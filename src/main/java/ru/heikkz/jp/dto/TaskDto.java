package ru.heikkz.jp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * DTO для задачи
 */
@Getter
@Setter
public class TaskDto {

    /**
     * ID таски
     */
    private Long id;
    /**
     * Заголовок
     */
    @NotBlank
    private String name;
    /**
     * Описание
     */
    private String description;
    /**
     * Тип
     */
    @NotBlank
    private String type;

}
