package tvz.ntpr.core.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class EmailService {
    private static final String ENCODING = "UTF-8";

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body, List<File> attachments) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        if (!attachments.isEmpty()) {
            for (File file : attachments)
                helper.addAttachment(file.getName(), file);
        }

        mailSender.send(message);
    }
}
