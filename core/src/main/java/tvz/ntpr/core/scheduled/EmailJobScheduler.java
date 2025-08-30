package tvz.ntpr.core.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tvz.ntpr.core.config.AppProperties;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.entity.Student;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.CronService;
import tvz.ntpr.core.utils.EmailService;

import java.io.File;
import java.util.List;

import static tvz.ntpr.core.config.Urls.URL_STUDENT;
import static tvz.ntpr.core.utils.HtmlToPdf.scrapeHtmlToPdf;

@Service
public class EmailJobScheduler {
    private final EmailService emailService;
    private final CronService cronService;
    private final Messages messages;
    private final AuthenticationService authenticationService;
    private final AppProperties appProperties;

    public EmailJobScheduler(EmailService emailService,
                             CronService cronService,
                             Messages messages,
                             AuthenticationService authenticationService,
                             AppProperties appProperties) {
        this.emailService = emailService;
        this.cronService = cronService;
        this.messages = messages;
        this.authenticationService = authenticationService;
        this.appProperties = appProperties;
    }

    //    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "#{appProperties.emailCron}")
    public void sendStudentReports() {
        List<Student> students = cronService.getAllStudents();

        authenticationService.cron();
        for (Student student : students) {
            try {
                File pdfData = scrapeHtmlToPdf(appProperties.getApplicationUrl() + URL_STUDENT, student.getId());
                String studentEmail = cronService.getEmailByUserId(student.getId());
//                String studentEmail = "tvz.java.web.app@gmail.com";

                emailService.sendEmail(studentEmail,
                        messages.getMessage("student-report.subject"),
                        messages.getMessage("student-report.text"),
                        List.of(pdfData));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
