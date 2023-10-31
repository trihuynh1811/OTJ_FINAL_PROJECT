package com.example.FAMS.service_implementors;

import com.example.FAMS.models.EmailDetails;
import com.example.FAMS.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    MimeMessage mimeMessage;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendMail(EmailDetails details) {
        try {
            mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper;

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setSubject(details.getSubject());

            String message = readFormat(details.getMsgBody());

            mimeMessage.setContent(message, "text/html; charset=utf-8");

            javaMailSender.send(mimeMessage);

            return "Mail sent successfully";
        } catch (Exception e) {
            return "Mail sent fail";
        }
    }

    private String readFormat(String randomPass) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader("src/main/java/com/example/FAMS/files/MailFormat.txt"));
        String line;
        StringBuilder result = new StringBuilder();
        while((line = br.readLine()) != null){
            result.append(line);
        }
        String[] arr = result.toString().split("@@@@@@@@######");
        if(arr.length == 2){
            result = new StringBuilder(arr[0] + randomPass + arr[1]);
        }
        return result.toString();
    }

}
