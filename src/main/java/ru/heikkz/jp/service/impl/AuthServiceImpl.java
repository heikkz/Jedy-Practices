package ru.heikkz.jp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.heikkz.jp.dto.RefreshTokenRequest;
import ru.heikkz.jp.entity.Role;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.entity.auth.VerificationToken;
import ru.heikkz.jp.exception.CoreException;
import ru.heikkz.jp.exception.MyBadRequestException;
import ru.heikkz.jp.model.NotificationEmail;
import ru.heikkz.jp.repository.UserRepository;
import ru.heikkz.jp.repository.VerificationTokenRepository;
import ru.heikkz.jp.dto.AuthenticationResponse;
import ru.heikkz.jp.dto.LoginRequest;
import ru.heikkz.jp.security.jwt.JwtProvider;
import ru.heikkz.jp.service.AuthService;
import ru.heikkz.jp.service.MailService;
import ru.heikkz.jp.service.RefreshTokenService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Value("${jp.accountVerificationUrl}")
    private String accountVerificationUrl;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                           RefreshTokenService refreshTokenService, VerificationTokenRepository verificationTokenRepository,
                           MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailService = mailService;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Transactional
    @Override
    public User register(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new MyBadRequestException("Выберите другой email");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        User createdUser = userRepository.save(user);
        String token = generateVerificationToken(user);
        String confirmPath = accountVerificationUrl + "/" + token;
        mailService.sendMail(new NotificationEmail("Пожалуйста активируйте свой аккаунт", user.getEmail(), confirmPath));
        return createdUser;
    }

    @Transactional
    @Override
    public void verifyEmailToken(String reqToken) {
        VerificationToken token = verificationTokenRepository.findByToken(reqToken)
                .orElseThrow(() -> new CoreException("Невалидный токен подтверждения регистрации"));
        User user = userRepository.findByEmail(token.getUser().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtProvider.generateToken(auth);
        return AuthenticationResponse.builder()
                .authenticationToken(jwtToken)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(request.getEmail())
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        refreshTokenService.validateToken(request.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(request.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(request.getUsername())
                .build();
    }

    /**
     * Сгенерировать токен подтверждения, для активации пользователя
     * @param user пользователь
     * @return токен подтверждения
     */
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
