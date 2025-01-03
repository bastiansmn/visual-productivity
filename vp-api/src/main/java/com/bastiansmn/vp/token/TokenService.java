package com.bastiansmn.vp.token;

import com.bastiansmn.vp.user.UserDAO;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public interface TokenService {

    String createAccessToken(String subject, List<String> authorities, Boolean enabled, Boolean notLocked);

    String createRefreshToken(String subject);

    void createJWTAndAddInHeaders(UserDAO user, HttpServletResponse response, Boolean secure);

}
