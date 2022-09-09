package com.bastiansmn.vp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody User u) {
        if (userService.emailExists(u.getEmail()))
            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    String.format("User with email %s already exists", u.getEmail())
                            )
                    );
        if (!validateUser(u))
            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "User is not valid"
                            )
                    );

        return ResponseEntity.ok(userService.create(u));
    }

    @GetMapping("/fetchById")
    public ResponseEntity<User> fetchById(@Param("id") Long id) {
        var user = userService.fetchById(id);
        return user.map(
                ResponseEntity::ok
        ).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User u) {
        if (validateUser(u))
            return ResponseEntity.badRequest()
                    .body("User is not valid");

        return ResponseEntity.ok(userService.update(u));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        if (userService.fetchById(id).isPresent())
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    private boolean validateUser(User u) {
        return u.getPassword().length() > 8
            && u.getEmail().matches("^[a-zA-Z\\d._-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$");
    }

}
