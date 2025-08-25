package tvz.ntpr.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NtprCoreApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NtprCoreApplication.class);
        app.setAdditionalProfiles("ntpr");
        app.run(args);
    }
}
