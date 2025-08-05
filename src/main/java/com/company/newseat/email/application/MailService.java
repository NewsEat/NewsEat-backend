package com.company.newseat.email.application;

import com.company.newseat.email.domain.vo.Mail;
import com.company.newseat.global.config.MailProperties;
import com.company.newseat.global.exception.code.status.ErrorStatus;
import com.company.newseat.global.exception.handler.MailHandler;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private Mail mail;

    public void sendMail(String to, Mail mail) {
        try {
            this.mail = mail;
            MimeMessage message = createMessage(to);
            javaMailSender.send(message);
        } catch (MailException | MessagingException | UnsupportedEncodingException es) {
            throw new MailHandler(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }

    private MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        final String from = mailProperties.username();
        final String fromName = mailProperties.name();

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject(mail.getTitle());
        message.setText(setContext(), "utf-8", "html");
        message.setFrom(new InternetAddress(from, fromName));

        return message;
    }

    private String setContext() {
        Context context = new Context();
        Map<String, String> values = mail.getValues();

        if (!values.isEmpty()) {
            for (String key : values.keySet()) {
                context.setVariable(key, values.get(key));
            }
        }
        return springTemplateEngine.process(mail.getTemplateName(), context);
    }
}
