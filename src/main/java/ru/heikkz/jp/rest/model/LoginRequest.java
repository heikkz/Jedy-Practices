package ru.heikkz.jp.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * DTO регистрации, аутентификации пользователя
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @NotNull(message = "E-mail не может быть пустым")
    @Email(message = "Неправильный e-mail")
    private String email;
    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 5, message = "Минимальная длина пароля - 5 символов")
    private String password;
}
