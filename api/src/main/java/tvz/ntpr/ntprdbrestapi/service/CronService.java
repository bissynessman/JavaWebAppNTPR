package tvz.ntpr.ntprdbrestapi.service;

import org.springframework.stereotype.Service;
import tvz.ntpr.ntprdbrestapi.entity.Student;
import tvz.ntpr.ntprdbrestapi.repo.StudentRepository;

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
