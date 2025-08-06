package tvz.ntpr.ntprproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NtprProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(NtprProjectApplication.class, args);
    }
}
