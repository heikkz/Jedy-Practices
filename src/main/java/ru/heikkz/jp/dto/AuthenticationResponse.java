package ru.heikkz.jp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * Ответ на логин пользователя
 */
@Data
@AllArgsConstructor
@Builder
public class AuthenticationResponse {

    /**
     * JWT-токен
     */
    private String authenticationToken;
    /**
     * Рефреш-токен
     */
    private String refreshToken;
    /**
     * Дата валидности
     */
    private Instant expiresAt;
    /**
     * Имя пользователя
     */
    private String username;
}
