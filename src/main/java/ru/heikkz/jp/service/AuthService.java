package ru.heikkz.jp.service;

import ru.heikkz.jp.dto.RefreshTokenRequest;
import ru.heikkz.jp.entity.User;
import ru.heikkz.jp.dto.AuthenticationResponse;
import ru.heikkz.jp.dto.LoginRequest;

/**
 * Сервис регистрации/аутентификации пользователя
 */
public interface AuthService {

    /**
     * Найти пользователя по email
     * @param email почтовый адрес
     * @return информация о пользователе
     */
    User findByEmail(String email);

    /**
     * Регистрация пользователя
     * @param request информация для регистрации
     * @return информация о пользователе
     */
    User register(LoginRequest request);

    /**
     * Верифицировать пользователя, после регистрации, по токену
     * @param token токен верификации
     */
    void verifyEmailToken(String token);

    /**
     * Аутентификация пользователя в системе
     * @param request информация для логина
     * @return ответ
     */
    AuthenticationResponse login(LoginRequest request);

    /**
     * Обновить JWT-токен
     * @param request запрос
     * @return ответ
     */
    AuthenticationResponse refreshToken(RefreshTokenRequest request);

    // TODO добавить восстановление пароля
}
