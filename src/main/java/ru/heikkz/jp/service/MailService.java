package ru.heikkz.jp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.heikkz.jp.exception.CoreException;
import ru.heikkz.jp.model.NotificationEmail;

/**
 * Сервис отправки писем
 */
@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    /**
     * Отправить письмо
     * @param email письмо
     */
    @Async
    public void sendMail(NotificationEmail email) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            // TODO изменить почтовый адрес
            messageHelper.setFrom("tempmail@gmail.com");
            messageHelper.setTo(email.getRecipient());
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(build(email.getRecipient(), email.getBody()));
        };
        try {
            mailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            throw new CoreException("Ошибка при отправке письма для: " + email.getRecipient());
        }
    }

    /**
     * Сгенерировать html-страницу письма
     * @param subject получатель письма
     * @param message сообщение
     * @return html-страница письма
     */
    private String build(String subject, String message){
        Context ctx = new Context();
        ctx.setVariable("emailTo", subject);
        ctx.setVariable("confirmUrl", message);
        return templateEngine.process("mailTemplate", ctx);
    }
}
