package com.bastiansmn.vp.user;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.user.dto.UserCreationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDAO> create(@RequestBody UserCreationDTO userDTO)
            throws FunctionalException, TechnicalException {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/v1/user/register")
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(this.userService.create(userDTO));
    }

    @GetMapping("/fetchByEmail")
    @PreAuthorize("#email.equals(authentication.principal) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDAO> fetchByEmail(@Param("email") String email) throws FunctionalException {
        return ResponseEntity.ok(this.userService.fetchByEmail(email));
    }

    @GetMapping("/fetchAll")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDAO>> fetchAll() {
        return ResponseEntity.ok(this.userService.fetchAll());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestBody UserDAO userDAO) throws FunctionalException {
        this.userService.delete(userDAO);
        return ResponseEntity.ok().build();
    }

}
