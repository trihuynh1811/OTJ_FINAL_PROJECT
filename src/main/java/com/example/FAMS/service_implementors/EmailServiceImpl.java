package com.example.FAMS.service_implementors;

import com.example.FAMS.FileUtils.TxtFileHandler;
import com.example.FAMS.models.EmailDetails;
import com.example.FAMS.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.chrono.MinguoChronology;

@Service
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;


    MimeMessage mimeMessage;

    @Override
    public String sendMail(EmailDetails details) {
        try {
            mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper;

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setSubject(details.getSubject());
            String message = TxtFileHandler.readMailFormatFile(details.getMsgBody());

            mimeMessage.setContent(message, "text/html; charset=utf-8");

            javaMailSender.send(mimeMessage);

            return "Mail sent successfully";
        } catch (Exception e) {
            return "Mail sent fail";
        }
    }


}
