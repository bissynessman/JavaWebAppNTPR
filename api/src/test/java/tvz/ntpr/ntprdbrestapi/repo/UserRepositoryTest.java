package tvz.ntpr.ntprdbrestapi.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import tvz.ntpr.ntprdbrestapi.entity.User;
import tvz.ntpr.ntprdbrestapi.enums.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("junit")
class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    User mockUser, mockUser2;

    @BeforeEach
    void setUp() {
        mockUser = new User("testuuid", "testusername", "testpassword", Role.STUDENT, "testuseruuid");
        mockUser2 = new User("testuuid2", "testusername2", "testpassword2", Role.PROFESSOR, "testuseruuid2");
        userRepository.create(mockUser);
        userRepository.create(mockUser2);
    }

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void findByUsername() {
        User user = userRepository.findByUsername("testusername");

        assertEquals(mockUser.getId(), user.getId());
    }
}