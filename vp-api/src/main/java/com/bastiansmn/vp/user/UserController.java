package com.bastiansmn.vp.user;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.user.dto.UserCreationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<UserDAO> create(@RequestBody UserCreationDTO userDTO) throws FunctionalException {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/user/create")
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(this.userService.create(userDTO));
    }

    @GetMapping("/fetchById")
    public ResponseEntity<UserDAO> fetchById(@Param("id") Long id) throws FunctionalException {
        return ResponseEntity.ok(this.userService.fetchByID(id));
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<UserDAO>> fetchAll() {
        return ResponseEntity.ok(this.userService.fetchAll());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@Param("id") Long id) throws FunctionalException {
        this.userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
