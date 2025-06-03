package com.jobrecruitment.service.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser_setsDefaultRoleAndEncodesPassword() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("plain");

        boolean result = userService.registerUser(user);

        User saved = userRepository.findByEmail("john@example.com").orElse(null);
        assertThat(result).isTrue();
        assertThat(saved).isNotNull();
        assertThat(saved.getRole()).isEqualTo(User.Role.APPLICANT);
        assertThat(passwordEncoder.matches("plain", saved.getPassword())).isTrue();
    }

    @Test
    void registerUser_returnsFalseWhenEmailExists() {
        User user1 = new User();
        user1.setName("Jane");
        user1.setEmail("jane@example.com");
        user1.setPassword("pass");
        userService.registerUser(user1);

        User user2 = new User();
        user2.setName("Jane2");
        user2.setEmail("jane@example.com");
        user2.setPassword("other");

        boolean result = userService.registerUser(user2);
        assertThat(result).isFalse();
    }

    @Test
    void authenticateUser_returnsTrueForValidCredentials() {
        User user = new User();
        user.setName("Mark");
        user.setEmail("mark@example.com");
        user.setPassword("secret");
        userService.registerUser(user);

        boolean auth = userService.authenticateUser("mark@example.com", "secret");
        assertThat(auth).isTrue();
    }

    @Test
    void authenticateUser_returnsFalseForInvalidCredentials() {
        User user = new User();
        user.setName("Paul");
        user.setEmail("paul@example.com");
        user.setPassword("secret");
        userService.registerUser(user);

        boolean auth = userService.authenticateUser("paul@example.com", "wrong");
        assertThat(auth).isFalse();
    }
}
