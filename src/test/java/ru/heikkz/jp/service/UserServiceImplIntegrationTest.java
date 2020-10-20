package ru.heikkz.jp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.heikkz.jp.entity.Role;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.entity.auth.VerificationToken;
import ru.heikkz.jp.exception.MyBadRequestException;
import ru.heikkz.jp.repository.UserRepository;
import ru.heikkz.jp.repository.VerificationTokenRepository;
import ru.heikkz.jp.dto.LoginRequest;
import ru.heikkz.jp.service.impl.AuthServiceImpl;
import ru.heikkz.jp.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// TODO дописать тесты
@ExtendWith(MockitoExtension.class)
class UserServiceImplIntegrationTest {

    @InjectMocks
    private AuthServiceImpl authService;
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private MailService mailService;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String encryptedPassword = "74hghd8474jf";

    /**
     * Ошибка при регистрации клиента, когда email уже занят
     */
    @Test
    public void whenCreateUser_ifEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getTestUser()));
        assertThrows(MyBadRequestException.class, () -> {
            authService.register(new LoginRequest("testuser@mail.ru", "1"));
        });
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Удачная регистрация клиента
     */
    @Test
    public void whenSaveUser_thenSuccess() {
        LoginRequest request = getLoginRequest();
        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(bCryptPasswordEncoder.encode(anyString())).willReturn(encryptedPassword);
        given(verificationTokenRepository.save(any(VerificationToken.class))).willReturn(any(VerificationToken.class));

        User savedUser = authService.register(request);
        assertThat(savedUser).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    /**
     * Удачное обновление клиента
     */
    @Test
    public void whenUpdateUser_thenSuccess() {
        User requestUser = getTestUser();
        User dbUser = getTestUserFromDb();
        given(userRepository.findByEmail(requestUser.getEmail())).willReturn(Optional.of(dbUser));
        given(userRepository.save(requestUser)).willAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.update(requestUser);
        assertEquals(requestUser.getEmail(), updated.getEmail());
        assertEquals(requestUser.getPassword(), updated.getPassword());
        verify(userRepository).save(any(User.class));
    }

    /**
     * Ошибка при обновлении информации о клиенте, когда клиент не найден
     */
    @Test
    public void whenUpdateUser_thenErrorIfUserNotExists() {
        User requestUser = getTestUser();
        given(userRepository.findByEmail(requestUser.getEmail())).willReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.update(requestUser);
        });
        verify(userRepository, never()).save(any(User.class));
    }


    public void shouldDelete() {
        long userId = 1L;
        userService.delete(userId);
        userService.delete(userId);

        verify(userRepository, times(2)).deleteById(userId);
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setCreated(LocalDate.of(2020, 10, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        user.setEmail("testuser@mail.ru");
        user.setEnabled(true);
        user.setPassword("123456");
        user.setRole(Role.USER);
        return user;
    }

    private User getTestUserFromDb() {
        User user = new User();
        user.setId(1L);
        user.setCreated(LocalDate.of(2020, 10, 17).atStartOfDay().toInstant(ZoneOffset.UTC));
        user.setEmail("user@mail.ru");
        user.setEnabled(true);
        user.setPassword("58585");
        user.setRole(Role.USER);
        return user;
    }

    private LoginRequest getLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@user.ru");
        request.setPassword("123456");
        return request;
    }

}
