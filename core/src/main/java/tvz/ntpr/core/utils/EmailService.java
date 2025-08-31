package tvz.ntpr.core.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tvz.ntpr.core.helper.Mail;

import java.io.File;

@Service
public class EmailService {
    private static final String ENCODING = "UTF-8";

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body, File attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.addAttachment(attachment.getName(), attachment);

        mailSender.send(message);
    }

    public void sendEmail(Mail mail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

        helper.setTo("tvz.java.web.app@gmail.com");
//        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getBody(), true);
        for (MultipartFile attachment : mail.getAttachments())
            helper.addAttachment(attachment.getOriginalFilename(), attachment);

        mailSender.send(message);
    }
}
