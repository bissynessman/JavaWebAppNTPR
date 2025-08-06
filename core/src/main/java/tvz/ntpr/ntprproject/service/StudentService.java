package tvz.ntpr.ntprproject.service;

import tvz.ntpr.ntprproject.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAll();
    Student getStudentById(String id);
    void saveStudent(Student student);
    void updateStudent(Student student);
    void deleteStudents(List<String> ids);
}
