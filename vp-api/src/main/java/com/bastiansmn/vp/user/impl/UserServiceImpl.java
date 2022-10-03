package com.bastiansmn.vp.user.impl;

import com.bastiansmn.vp.authorities.AuthoritiesService;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.role.RoleService;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserPrincipal;
import com.bastiansmn.vp.user.UserRepository;
import com.bastiansmn.vp.user.UserService;
import com.bastiansmn.vp.user.dto.UserCreationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AuthoritiesService authoritiesService;
    private final PasswordEncoder passwordEncoder;

    public UserDAO create(UserCreationDTO userDTO) throws FunctionalException {
        if (this.emailExists(userDTO.getEmail()))
            throw new FunctionalException(FunctionalRule.USER_0001);

        if (this.usernameExists(userDTO.getUsername()))
            throw new FunctionalException(FunctionalRule.USER_0002);

        if (!this.validateMail(userDTO.getEmail()))
            throw new FunctionalException(FunctionalRule.USER_0003);

        if (!this.validatePassword(userDTO.getPassword()))
            throw new FunctionalException(FunctionalRule.USER_0004);

        List<RoleDAO> defaultRoles = this.roleService.getDefaultRoles();

        UserDAO user = UserDAO.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .name(userDTO.getName())
                .lastname(userDTO.getLastname())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(defaultRoles)
                .isEnabled(true)
                .isNotLocked(true)
                .build();

        log.info("Creating user: {}", user);
        return this.userRepository.save(user);
    }

    public UserDAO fetchByID(Long id) throws FunctionalException {
        var user = this.userRepository.findById(id);

        if (user.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.USER_0005, HttpStatus.NOT_FOUND);

        log.info("Fetching user by id: {}", id);
        return user.get();
    }

    public UserDAO fetchByUsername(String username) throws FunctionalException {
        var user = this.userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.USER_0005, HttpStatus.NOT_FOUND);

        log.info("Fetching user by username: {}", username);
        return user.get();
    }

    public UserDAO fetchByEmail(String email) throws FunctionalException {
        var user = this.userRepository.findByEmail(email);

        if (user.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.USER_0005, HttpStatus.NOT_FOUND);

        log.info("Fetching user by email: {}", email);

        return user.get();
    }

    public List<UserDAO> fetchAll() {
        log.info("Fetching all users");
        return this.userRepository.findAll();
    }

    public void delete(Long id) throws FunctionalException {
        Optional<UserDAO> user = this.userRepository.findById(id);
        if (user.isEmpty())
            throw new FunctionalException(FunctionalRule.USER_0005, HttpStatus.NOT_FOUND);

        log.info("Deleting user with id={}", id);
        this.userRepository.deleteById(id);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isEnabled(String email) throws FunctionalException {
        return this.fetchByEmail(email).isEnabled();
    }

    public boolean isNotLocked(String email) throws FunctionalException {
        return this.fetchByEmail(email).isNotLocked();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug(email);
        UserDAO userDAO = userRepository
                .findByEmail(email)
                .orElseThrow();
        return new UserPrincipal(userDAO);
    }

    private boolean validatePassword(String password) {
        return password.length() > 8;
    }

    private boolean validateMail(String mail) {
        return mail.matches("^[a-zA-Z\\d._-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$");
    }

}
