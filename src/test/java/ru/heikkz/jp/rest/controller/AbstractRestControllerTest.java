package ru.heikkz.jp.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class AbstractRestControllerTest {

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Getter
    @NoArgsConstructor
    static class ErrorResult {
        private final List<FieldValidationError> fieldErrors = new ArrayList<>();
        ErrorResult(String field, String message){
            this.fieldErrors.add(new FieldValidationError(field, message));
        }
    }

    @Getter
    @AllArgsConstructor
    static class FieldValidationError {
        private String field;
        private String message;
    }
}
