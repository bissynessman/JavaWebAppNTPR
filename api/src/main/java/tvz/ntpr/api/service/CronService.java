package tvz.ntpr.api.service;

import org.springframework.stereotype.Service;
import tvz.ntpr.api.entity.Student;
import tvz.ntpr.api.repo.StudentRepository;

import java.util.List;

@Service
public class CronService {
    private final StudentRepository studentRepository;

    public CronService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
