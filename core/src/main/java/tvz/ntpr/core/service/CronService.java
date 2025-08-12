package tvz.ntpr.core.service;

import tvz.ntpr.core.entity.Student;

import java.util.List;

public interface CronService {
    List<Student> getAllStudents();
    String getEmailByUserId(String userId);
}
