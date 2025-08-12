package tvz.ntpr.api.service;

import org.springframework.stereotype.Service;
import tvz.ntpr.api.entity.Student;
import tvz.ntpr.api.repo.StudentRepository;
import tvz.ntpr.api.repo.UserRepository;

import java.util.List;

@Service
public class CronService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public CronService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public String getEmailByUserId(String userId) {
        return userRepository.findEmailByUserUuid(userId);
    }
}
