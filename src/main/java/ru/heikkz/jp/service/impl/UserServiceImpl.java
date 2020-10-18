package ru.heikkz.jp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.heikkz.jp.entity.Role;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.entity.auth.VerificationToken;
import ru.heikkz.jp.exception.CoreException;
import ru.heikkz.jp.exception.MyBadRequestException;
import ru.heikkz.jp.repository.UserRepository;
import ru.heikkz.jp.repository.VerificationTokenRepository;
import ru.heikkz.jp.rest.model.LoginRequest;
import ru.heikkz.jp.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Transactional
    @Override
    public User create(LoginRequest request) {
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
        // TODO отправка письма
//        String token = generateVerificationToken(user);

        return createdUser;
    }

    @Override
    public User update(User user) {
        Optional<User> optional = userRepository.findByEmail(user.getEmail());
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException("Пользователь не найден");
        } else {
            User dbUser = optional.get();
            if (!dbUser.getId().equals(user.getId())) {
                // TODO добавить логгирование
                throw new MyBadRequestException("Неверные данные");
            }
            return userRepository.save(user);
        }
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public boolean verifyEmailToken(String token) {
        throw new CoreException("Unsupported operation");
    }

    @Override
    public boolean requestResetPassword(String email) {
        throw new CoreException("Unsupported operation");
    }

    @Override
    public boolean resetPassword(String token, String email) {
        throw new CoreException("Unsupported operation");
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findAll(int page, int count) {
        return null;
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