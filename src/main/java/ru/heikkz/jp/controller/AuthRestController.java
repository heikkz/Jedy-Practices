package ru.heikkz.jp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.heikkz.jp.dto.AuthenticationResponse;
import ru.heikkz.jp.dto.LoginRequest;
import ru.heikkz.jp.dto.RefreshTokenRequest;
import ru.heikkz.jp.service.AuthService;
import ru.heikkz.jp.service.RefreshTokenService;

import javax.validation.Valid;

// TODO контракт REST-ответов
@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    /* Сервис для работы с пользователями */
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthRestController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Регистрация пользователя в системе
     * @param request информация о пользователе
     * @return OK
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid LoginRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Подтверждение регистрации пользователя
     * @param token токен верификации регистрации
     * @return OK
     */
    @GetMapping("/signup/confirm/{token}")
    public ResponseEntity<String> confirmSignup(@PathVariable String token) {
        authService.verifyEmailToken(token);
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    /**
     * Аутентификация пользователя
     * @param request информация о пользователе
     * @return ответ на логин пользователя
     */
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /**
     * Обновить JWT-токен
     */
    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    /**
     * Удалить рефреш-токен из БД
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Valid RefreshTokenRequest request) {
        refreshTokenService.deleteToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!");
    }
}
