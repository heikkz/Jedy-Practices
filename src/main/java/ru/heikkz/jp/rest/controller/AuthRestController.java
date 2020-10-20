package ru.heikkz.jp.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.heikkz.jp.rest.model.AuthenticationResponse;
import ru.heikkz.jp.rest.model.LoginRequest;
import ru.heikkz.jp.service.UserService;

import javax.validation.Valid;

// TODO контракт REST-ответов
@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    /* Сервис для работы с пользователями */
    private final UserService userService;

    @Autowired
    public AuthRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Регистрация пользователя в системе
     * @param request информация о пользователе
     * @return OK
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid LoginRequest request) {
        userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Подтверждение регистрации пользователя
     * @param token токен верификации регистрации
     * @return OK
     */
    @GetMapping("/signup/confirm/{token}")
    public ResponseEntity<String> confirmSignup(@PathVariable String token) {
        userService.verifyEmailToken(token);
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
