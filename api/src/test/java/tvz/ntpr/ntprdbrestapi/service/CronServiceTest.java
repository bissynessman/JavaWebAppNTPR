package tvz.ntpr.ntprdbrestapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tvz.ntpr.ntprdbrestapi.entity.Student;
import tvz.ntpr.ntprdbrestapi.repo.StudentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CronServiceTest {
    @Mock Student student;
    @Mock Student student2;

    @Mock StudentRepository studentRepository;

    CronService cronService;

    @BeforeEach
    void setUp() {
        cronService = new CronService(studentRepository);
        student = new Student();
        student2 = new Student();
    }

    @Test
    void getAllStudents() {
        when(cronService.getAllStudents()).thenReturn(List.of(student, student2));

        List<Student> students = cronService.getAllStudents();

        assertEquals(2, students.size());
    }
}