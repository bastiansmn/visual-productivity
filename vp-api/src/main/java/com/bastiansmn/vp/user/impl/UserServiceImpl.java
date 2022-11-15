package com.bastiansmn.vp.user.impl;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.mail.MailConfirmService;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.role.RoleService;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserPrincipal;
import com.bastiansmn.vp.user.UserRepository;
import com.bastiansmn.vp.user.UserService;
import com.bastiansmn.vp.user.dto.UserCreationDTO;
import com.bastiansmn.vp.socialAuth.UserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MailConfirmService mailConfirmService;

    public UserDAO create(UserCreationDTO userDTO) throws FunctionalException, TechnicalException {
        // TODO Limiter ler nombre de requêtes par IP (car route publique)
        if (this.isSocialUser(userDTO.getEmail()))
            throw new FunctionalException(FunctionalRule.USER_0007);

        if (this.emailExists(userDTO.getEmail()))
            throw new FunctionalException(FunctionalRule.USER_0001);

        if (!this.validateMail(userDTO.getEmail()))
            throw new FunctionalException(FunctionalRule.USER_0003);

        if (userDTO.getProvider().equals(UserProvider.LOCAL) && !this.validatePassword(userDTO.getPassword()))
            throw new FunctionalException(FunctionalRule.USER_0004);

        Set<RoleDAO> defaultRoles = this.roleService.getDefaultRoles();

        UserDAO user = UserDAO.builder()
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .lastname(userDTO.getLastname())
                .password(
                    userDTO.getProvider().equals(UserProvider.LOCAL)
                        ? passwordEncoder.encode(userDTO.getPassword())
                        : null
                )
                .provider(userDTO.getProvider())
                .roles(defaultRoles)
                .createdDate(Date.from(Instant.now()))
                .isEnabled(false)
                .isNotLocked(true)
                .build();

        log.info("Creating user: {}", user);

        UserDAO createdUser = this.userRepository.save(user);
        this.mailConfirmService.createConfirmation(createdUser);
        return createdUser;
    }

    public UserDAO fetchByEmail(String email) throws FunctionalException {
        var user = this.userRepository.findByEmail(email);

        if (user.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.USER_0004, HttpStatus.NOT_FOUND);

        log.info("Fetching user by email: {}", email);

        return user.get();
    }

    public List<UserDAO> fetchAll() {
        log.info("Fetching all users");
        return this.userRepository.findAll();
    }

    public void delete(Long user_id) throws FunctionalException {
        Optional<UserDAO> user = this.userRepository.findById(user_id);
        if (user.isEmpty())
            throw new FunctionalException(FunctionalRule.USER_0004, HttpStatus.NOT_FOUND);

        log.info("Deleting user with id={}", user.get().getUser_id());
        this.userRepository.delete(user.get());
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
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

    private boolean isSocialUser(String email) {
        Optional<UserDAO> user = this.userRepository.findByEmail(email);
        if (user.isEmpty()) return false;
        return !user.get().getProvider().equals(UserProvider.LOCAL);
    }

}
