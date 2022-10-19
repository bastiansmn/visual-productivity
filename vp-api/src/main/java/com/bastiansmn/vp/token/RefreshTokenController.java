package com.bastiansmn.vp.token;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.user.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("${api.prefix}/token")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/refresh")
    public ResponseEntity<UserDAO> refresh(HttpServletRequest request, HttpServletResponse response) throws FunctionalException {
        return ResponseEntity.ok(this.refreshTokenService.refresh(request, response));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(HttpServletRequest request) throws FunctionalException {
        return ResponseEntity.ok(this.refreshTokenService.validate(request));
    }

}
