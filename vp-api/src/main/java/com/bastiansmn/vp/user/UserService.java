package com.bastiansmn.vp.user;

import com.bastiansmn.vp.shared.CrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements CrudService<User, Long> {

    private final UserRepository userRepository;

    @Override
    public User create(User u) {
        log.info("Creating user: {}", u);
        return userRepository.save(u);
    }

    @Override
    public Optional<User> fetchById(Long id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public User update(User u) {
        log.info("Updating user: {}", u);
        return userRepository.save(u);
    }

    @Override
    public boolean delete(Long id) {
        if (userRepository.existsById(id)) {
            log.info("Deleting user by id: {}", id);
            userRepository.deleteById(id);
            return true;
        }
        log.info("Delete user: User with id {} does not exist", id);
        return false;
    }

    @Override
    public List<User> fetchAll() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

}
