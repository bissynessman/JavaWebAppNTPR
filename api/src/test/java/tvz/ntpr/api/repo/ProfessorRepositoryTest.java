package tvz.ntpr.api.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import tvz.ntpr.api.entity.Professor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("junit")
class ProfessorRepositoryTest {
    @Autowired ProfessorRepository professorRepository;

    Professor mockProfessor, mockProfessor2;

    @BeforeEach
    void setUp() {
        mockProfessor = new Professor("testuuid", "test", "professor", true);
        mockProfessor2 = new Professor("testuuid2", "test2", "professor2", false);
        professorRepository.create(mockProfessor);
        professorRepository.create(mockProfessor2);
    }

    @Test
    void findAll() {
        List<Professor> professors = professorRepository.findAll();

        assertEquals(2, professors.size());
    }

    @Test
    void findAllUnauthorized() {
        List<Professor> professors = professorRepository.findAllUnauthorized();

        assertEquals(mockProfessor2.getId(), professors.getFirst().getId());
    }

    @Test
    void authorize() {
        assertFalse(mockProfessor2.isAuthorized());

        professorRepository.authorize(mockProfessor2.getId());

        mockProfessor2 = professorRepository.findById(mockProfessor2.getId()).get();

        assertTrue(mockProfessor2.isAuthorized());
    }
}