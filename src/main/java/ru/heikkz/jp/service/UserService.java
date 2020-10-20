package ru.heikkz.jp.service;

import org.springframework.data.domain.Page;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.rest.model.AuthenticationResponse;
import ru.heikkz.jp.rest.model.LoginRequest;

import java.util.List;

public interface UserService {

    User findByEmail(String email);

    User create(LoginRequest request);

    User update(User user);

    User findById(Long id);

    void delete(Long id);

    /**
     * Верифицировать пользователя, после регистрации, по токену
     * @param token токен верификации
     */
    void verifyEmailToken(String token);

    boolean requestResetPassword(String email);

    boolean resetPassword(String token, String email);

    List<User> findAll();

    Page<User> findAll(int page, int count);

    AuthenticationResponse login(LoginRequest request);
}
