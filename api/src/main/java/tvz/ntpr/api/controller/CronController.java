package tvz.ntpr.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tvz.ntpr.api.entity.Student;
import tvz.ntpr.api.service.CronService;

import java.util.List;

import static tvz.ntpr.api.config.Urls.URL_CRON;

@RestController
@RequestMapping(URL_CRON)
public class CronController {
    private final CronService cronService;

    public CronController(CronService cronService) {
        this.cronService = cronService;
    }

    @GetMapping
    public List<Student> cron() {
        return cronService.getAllStudents();
    }
}
