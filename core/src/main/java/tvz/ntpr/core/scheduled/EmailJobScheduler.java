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
import tvz.ntpr.core.service.UserService;

import java.io.ByteArrayInputStream;
import java.util.List;

import static tvz.ntpr.core.utils.HtmlToPdf.scrapeHtmlToPdfByteArray;
import static tvz.ntpr.core.config.Urls.BASE_URL;
import static tvz.ntpr.core.config.Urls.URL_STUDENT;

@Service
public class EmailJobScheduler {
    private final JavaMailSender mailSender;
    private final CronService cronService;
    private final Messages messages;
    private final AuthenticationService authenticationService;

    public EmailJobScheduler(JavaMailSender mailSender,
                             CronService cronService,
                             Messages messages,
                             AuthenticationService authenticationService) {
        this.mailSender = mailSender;
        this.cronService = cronService;
        this.messages = messages;
        this.authenticationService = authenticationService;
    }

//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 0 12 1 * ?")
    public void sendStudentReports() {
        List<Student> students = cronService.getAllStudents();

        authenticationService.cron();
        for (Student student : students) {
            byte[] pdfData = scrapeHtmlToPdfByteArray(BASE_URL + URL_STUDENT, student.getId());
            String studentEmail = cronService.getEmailByUserId(student.getId());
//            studentEmail = "tvz.java.web.app@gmail.com";
            sendEmail(studentEmail, pdfData);
        }
    }

    private void sendEmail(String to, byte[] pdfData) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(messages.getMessage("student-report.subject"));
            helper.setText(messages.getMessage("student-report.text"));
            helper.addAttachment("student-report.pdf", () -> new ByteArrayInputStream(pdfData), "application/pdf");

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
