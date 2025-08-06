package tvz.ntpr.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NtprCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(NtprCoreApplication.class, args);
    }
}
