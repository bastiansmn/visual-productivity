package com.bastiansmn.vp.user;

import com.bastiansmn.vp.shared.CrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDAO create(UserDAO u) {
        log.info("Creating user: {}", u);
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return userRepository.save(u);
    }

    public Optional<UserDAO> fetchById(Long id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id);
    }

    public UserDAO update(UserDAO u) {
        log.info("Updating user: {}", u);
        return userRepository.save(u);
    }

    public void delete(Long id) {
        if (userRepository.existsById(id)) {
            log.info("Deleting user by id: {}", id);
            userRepository.deleteById(id);
            return;
        }
        log.info("Delete user: User with id {} does not exist", id);
    }

    public List<UserDAO> fetchAll() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDAO userDAO = userRepository
                .findByUsername(username)
                .orElseThrow();
        return User
                .withUsername(userDAO.getUsername())
                .password(userDAO.getPassword())
                .build();
    }

}
