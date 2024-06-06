package com.sparta.wildcard_newsfeed.domain.user.service;

import com.sparta.wildcard_newsfeed.domain.user.dto.EmailSendEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    private static final String SENDER_EMAIL = "b15wildcard@gmail.com";


    @Async
    @TransactionalEventListener
    public void sendEmail(EmailSendEvent emailSendEvent) {
        log.info("메일 전송 시도 ");
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setFrom(SENDER_EMAIL);
            mimeMessageHelper.setTo(emailSendEvent.getTo());
            mimeMessageHelper.setSubject(emailSendEvent.getSubject());
            mimeMessageHelper.setText(emailSendEvent.getBody(), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("메일 전송 실패 ", e);
        }
    }
}
