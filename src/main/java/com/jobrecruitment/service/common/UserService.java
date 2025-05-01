package com.jobrecruitment.service.common;

import com.jobrecruitment.model.User;
import com.jobrecruitment.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean registerUser(User user) {
        if (user.getRole() == null) {
            user.setRole(User.Role.APPLICANT);
        }
        logger.info("Registering user with role: {}", user.getRole());

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return false;
        }

        user.setPassword(encodePassword(user.getPassword()));

        userRepository.save(user);
        return true;
    }

    public boolean authenticateUser(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            logger.info("User not found");
            return false;
        }

        User user = userOpt.get();
        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!matches) {
            logger.info("Password doesn't match!");
        } else {
            logger.info("Login successful!");
        }
        return matches;
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
