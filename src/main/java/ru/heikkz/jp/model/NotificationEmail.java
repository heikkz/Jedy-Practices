package ru.heikkz.jp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сообщение, отправляемое по email
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {

    /**
     * Тема письма
     */
    private String subject;
    /**
     * Получатель
     */
    private String recipient;
    /**
     * Текст сообщения
     */
    private String body;
}
