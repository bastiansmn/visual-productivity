package com.bastiansmn.vp.user;

import com.bastiansmn.vp.authorities.AuthoritiesDAO;
import com.bastiansmn.vp.authorities.AuthoritiesService;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.role.RoleService;
import com.bastiansmn.vp.shared.BodyResponse;
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
    private final RoleService roleService;
    private final AuthoritiesService authoritiesService;


    @PostMapping("/create")
    public ResponseEntity<BodyResponse<UserDAO>> create(@RequestBody UserCreationDTO userDTO) {
        if (userService.emailExists(userDTO.getEmail()))
            return ResponseEntity
                    .badRequest()
                    .body(
                            BodyResponse.<UserDAO>builder()
                                    .message("Un utilisateur avec cet email existe déjà")
                                    .build()
                    );

        if (userService.usernameExists(userDTO.getUsername()))
            return ResponseEntity
                    .badRequest()
                    .body(
                            BodyResponse.<UserDAO>builder()
                                    .message("Un utilisateur avec ce nom d'utilisateur existe déjà")
                                    .build()
                    );

        List<RoleDAO> defaultRoles = (List<RoleDAO>) this.roleService.getDefaultRoles();
        List<AuthoritiesDAO> defaultAuthorities = (List<AuthoritiesDAO>) this.authoritiesService.getDefaultAuthorities();

        UserDAO user = UserDAO.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .name(userDTO.getName())
                .lastname(userDTO.getLastname())
                .password(userDTO.getPassword())
                .roles(defaultRoles)
                .authorities(defaultAuthorities)
                .isEnabled(true)
                .isNotLocked(true)
                .build();

        if (!validateUser(user))
            return ResponseEntity
                    .badRequest()
                    .body(
                            BodyResponse.<UserDAO>builder()
                                    .message("Le mot de passe ou l'email n'est pas valide")
                                    .build()
                    );

        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/user/create")
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(
                BodyResponse.<UserDAO>builder()
                        .message("Utilisateur créé")
                        .data(userService.create(user))
                        .build()
        );
    }

    @GetMapping("/fetchById")
    public ResponseEntity<BodyResponse<UserDAO>> fetchById(@Param("id") Long id) {
        var user = userService.fetchById(id);
        return user.map(u ->
                ResponseEntity
                        .ok()
                        .body(
                                BodyResponse.<UserDAO>builder()
                                        .data(user.get())
                                        .build()
                        )
        ).orElseGet(
                () -> ResponseEntity.notFound().build()
        );
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<BodyResponse<List<UserDAO>>> fetchAll() {
        return ResponseEntity.ok().body(
                BodyResponse.<List<UserDAO>>builder()
                        .data(this.userService.fetchAll())
                        .build()
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BodyResponse<?>> delete(@Param("id") Long id) {
        if (userService.fetchById(id).isPresent()) {
            userService.delete(id);
            return ResponseEntity.ok().body(
                BodyResponse.builder()
                        .message("Utilisateur supprimé")
                        .build()
            );
        }
        return ResponseEntity.notFound().build();
    }

    private boolean validateUser(UserDAO u) {
        return this.validateMail(u.getEmail()) && this.validatePassword(u.getPassword());
    }

    private boolean validatePassword(String password) {
        return password.length() > 8;
    }

    private boolean validateMail(String mail) {
        return mail.matches("^[a-zA-Z\\d._-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,6}$");
    }

}
