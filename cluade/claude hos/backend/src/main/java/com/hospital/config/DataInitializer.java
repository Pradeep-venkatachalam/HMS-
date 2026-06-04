package com.hospital.config;

import com.hospital.entity.Role;
import com.hospital.entity.User;
import com.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@hospital.com")
                    .fullName("System Administrator")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            log.info("Default admin user created: admin / admin123");
        }

        if (!userRepository.existsByUsername("receptionist")) {
            User receptionist = User.builder()
                    .username("receptionist")
                    .password(passwordEncoder.encode("recept123"))
                    .email("receptionist@hospital.com")
                    .fullName("Front Desk")
                    .role(Role.RECEPTIONIST)
                    .enabled(true)
                    .build();
            userRepository.save(receptionist);
            log.info("Default receptionist created: receptionist / recept123");
        }
    }
}
