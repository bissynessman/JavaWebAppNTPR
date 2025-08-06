package tvz.ntpr.ntprdbrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tvz.ntpr.ntprdbrestapi.entity.Student;
import tvz.ntpr.ntprdbrestapi.service.CronService;

import java.util.List;

@RestController
@RequestMapping("/cron")
public class CronController {
    @Autowired
    private CronService cronService;

    @GetMapping
    public List<Student> cron() {
        return cronService.getAllStudents();
    }
}
