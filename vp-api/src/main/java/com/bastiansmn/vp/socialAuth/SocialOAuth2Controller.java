package com.bastiansmn.vp.socialAuth;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.socialAuth.dto.SocialUserDTO;
import com.bastiansmn.vp.user.UserDAO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Slf4j
public class SocialOAuth2Controller {

    private final SocialOAuth2Service socialOAuth2Service;

    @Operation(summary = "Log l'utilisateur via Oauth2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur loggé"),
            @ApiResponse(responseCode = "403", description = "L'idToken de l'utilisateur est invalide"),
    })
    @PostMapping("/login")
    public ResponseEntity<UserDAO> socialLogin(@RequestBody SocialUserDTO userDTO, HttpServletResponse response)
            throws FunctionalException {
        log.debug("Processing OAuth2 login for '{}'", userDTO);
        return ResponseEntity.ok(this.socialOAuth2Service.login(userDTO, response));
    }

}
