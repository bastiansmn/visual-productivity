package com.bastiansmn.vp.user;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.user.dto.UserCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/${api.prefix}/${api.version}/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDAO> create(@RequestBody UserCreationDTO userDTO) throws FunctionalException {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/v1/user/register")
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(this.userService.create(userDTO));
    }

    @GetMapping("/fetchById")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDAO> fetchById(@Param("id") Long id) throws FunctionalException {
        return ResponseEntity.ok(this.userService.fetchByID(id));
    }

    @GetMapping("/fetchAll")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDAO>> fetchAll() {
        return ResponseEntity.ok(this.userService.fetchAll());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@Param("id") Long id) throws FunctionalException {
        this.userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
