package tvz.ntpr.ntprproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.ntpr.ntprproject.entity.Student;
import tvz.ntpr.ntprproject.rest.DatabaseAPI;
import tvz.ntpr.ntprproject.utils.JsonParser;

import java.util.List;

@Service
@Primary
public class CronServiceImpl implements CronService {
    private static final String API_URL = DatabaseAPI.CRON_API;

    private final RestTemplate restTemplate;

    @Autowired
    public CronServiceImpl(@Qualifier("cronRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = null;
        try{
            students = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL,
                            String.class).getBody(),
                    Student.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
