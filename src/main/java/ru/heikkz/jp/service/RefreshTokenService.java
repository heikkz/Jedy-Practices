package ru.heikkz.jp.service;

import ru.heikkz.jp.model.RefreshToken;

public interface RefreshTokenService {

    /**
     * Сгенерировать рефреш-токен
     * @return рефреш-токен
     */
    RefreshToken generateRefreshToken();

    /**
     * Валидировать рефреш-токен
     * @param token рефреш-токен
     */
    void validateToken(String token);

    /**
     * Удалить рефреш-токен
     * @param token рефреш-токен
     */
    void deleteToken(String token);
}
