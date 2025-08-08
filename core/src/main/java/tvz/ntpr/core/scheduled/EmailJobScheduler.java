package tvz.ntpr.core.scheduled;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.entity.Student;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.CronService;

import java.io.ByteArrayInputStream;
import java.util.List;

import static tvz.ntpr.core.utils.HtmlToPdf.scrapeHtmlToPdfByteArray;
import static tvz.ntpr.core.utils.Urls.URL_STUDENT;

@Service
public class EmailJobScheduler {
    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private final CronService cronService;
    @Autowired
    private final Messages messages;
    @Autowired
    private final AuthenticationService authenticationService;

    public EmailJobScheduler(JavaMailSender mailSender, CronService cronService, Messages messages, AuthenticationService authenticationService) {
        this.mailSender = mailSender;
        this.cronService = cronService;
        this.messages = messages;
        this.authenticationService = authenticationService;
    }

    @Scheduled(cron = "0 30 18 * * ?")
    public void sendStudentReports() {
        List<Student> students = cronService.getAllStudents();

        authenticationService.cron();
        for (Student student : students) {
            byte[] pdfData = scrapeHtmlToPdfByteArray("http://127.0.0.1:8080/ntpr" + URL_STUDENT, student.getId());
            String studentEmail = "tvz.java.web.app@gmail.com";
            try {
                sendEmail(studentEmail, pdfData);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendEmail(String to, byte[] pdfData) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(messages.getMessage("student-report.subject"));
        helper.setText(messages.getMessage("student-report.text"));
        helper.addAttachment("student-report.pdf", () -> new ByteArrayInputStream(pdfData), "application/pdf");

        mailSender.send(mimeMessage);
    }
}
