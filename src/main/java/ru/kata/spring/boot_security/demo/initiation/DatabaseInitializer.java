package ru.kata.spring.boot_security.demo.initiation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Role userRole = new Role();
        userRole.setRole("ROLE_USER");
        roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setRole("ROLE_ADMIN");
        roleRepository.save(adminRole);

        List<Role> userRoles = Arrays.asList(userRole);
        List<Role> adminRoles = Arrays.asList(adminRole, userRole);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(adminRoles);
        userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setRoles(userRoles);
        userRepository.save(user);
    }
}