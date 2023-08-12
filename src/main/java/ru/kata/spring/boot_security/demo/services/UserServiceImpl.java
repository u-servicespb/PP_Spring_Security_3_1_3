package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.models.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("Пользователь с таким именем не найден");
        }
        return userRepository.findByUsername(username).get();
    }

    @Override
    public User findUserById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("Пользователь с таким ID не найден");
        }
        return userRepository.findById(id).get();
    }

    @Transactional
    @Override
    public void updateUser(User updateUser, Long id) {
        User user_from_DB = userRepository.findById(id).get();
        user_from_DB.setUsername(updateUser.getUsername());
        user_from_DB.setRoles((List<Role>) updateUser.getAuthorities());

        if (user_from_DB.getPassword().equals(updateUser.getPassword())) {
            userRepository.save(user_from_DB);
        } else {
            user_from_DB.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            userRepository.save(user_from_DB);
        }
        userRepository.save(user_from_DB);
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean deleteUserById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}