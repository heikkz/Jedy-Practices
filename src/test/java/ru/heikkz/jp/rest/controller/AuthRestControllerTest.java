package ru.heikkz.jp.rest.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.heikkz.jp.exception.MyBadRequestException;
import ru.heikkz.jp.rest.model.LoginRequest;
import ru.heikkz.jp.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthRestController.class)
class AuthRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private static final String URL = "/api/v1/auth";
    private static final String SIGNUP = URL + "/signup";

    /**
     * Невалидные данные при регистрации
     * @throws Exception
     */
    @Test
    void whenNullValue_thenReturns400() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail(null);
        request.setPassword("123456");

        mvc.perform(post(SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Невалидные данные при регистрации с текстом ошибки
     * @throws Exception
     */
    @Test
    void whenNullValue_thenReturns400AndErrorResult() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("testuser@mail.ru");
        request.setPassword(null);

        MvcResult mvcResult = mvc.perform(post(SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResult expectedErrorResponse = new ErrorResult("password", "Пароль не может быть пустым");
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResponseBody = mapToJson(expectedErrorResponse);
        assertThat(actualResponseBody)
                .isEqualToIgnoringWhitespace(expectedResponseBody);
    }

    /**
     * Валидная регистрация
     * @throws Exception
     */
    @Test
    public void whenUserCreate_thenReturn200() throws Exception {
        mvc.perform(post(SIGNUP).contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(getLoginRequest())))
                .andExpect(status().isCreated());
    }

    /**
     * Ошибка когда email занят другим пользователем
     * @throws Exception
     */
    @Test
    public void whenUserExists_thenReturns400AndErrorResult() throws Exception {
        when(userService.create(ArgumentMatchers.any(LoginRequest.class)))
                .thenThrow(new MyBadRequestException("Выберите другой email"));

        MvcResult mvcResult = mvc.perform(post(SIGNUP).contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(getLoginRequest())))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResponseBody = "{\"status\":400,\"message\":\"Выберите другой email\",\"developerMessage\":\"MyBadRequestException\"}";
        assertEquals(actualResponseBody, expectedResponseBody);
    }

    private LoginRequest getLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@user.ru");
        request.setPassword("123456");
        return request;
    }
}
