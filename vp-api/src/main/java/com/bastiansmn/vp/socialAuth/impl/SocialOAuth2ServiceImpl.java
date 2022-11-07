package com.bastiansmn.vp.socialAuth.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bastiansmn.vp.config.SecurityConstant;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.role.RoleService;
import com.bastiansmn.vp.socialAuth.SocialOAuth2Service;
import com.bastiansmn.vp.socialAuth.UserProvider;
import com.bastiansmn.vp.socialAuth.dto.SocialUserDTO;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserRepository;
import com.bastiansmn.vp.utils.CookieUtils;
import com.bastiansmn.vp.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialOAuth2ServiceImpl implements SocialOAuth2Service {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public UserDAO login(SocialUserDTO userDTO, HttpServletResponse response) throws FunctionalException {
        if (!this.validateIdToken(userDTO.getIdToken(), userDTO.getProvider()))
            throw new FunctionalException(FunctionalRule.USER_0008, HttpStatus.FORBIDDEN);

        Optional<UserDAO> user = this.userRepository.findByEmail(userDTO.getEmail());
        UserDAO toAddUser;
        if (user.isEmpty()) {
            // Create the new user
            Set<RoleDAO> defaultRoles = this.roleService.getDefaultRoles();
            toAddUser = UserDAO.builder()
                    .email(userDTO.getEmail())
                    .name(userDTO.getFirstName())
                    .lastname(userDTO.getLastName())
                    .provider(
                            Arrays.stream(UserProvider.values())
                                    .filter(p -> p.name().equalsIgnoreCase(userDTO.getProvider()))
                                    .findFirst().orElse(UserProvider.LOCAL))
                    .roles(defaultRoles)
                    .isEnabled(Boolean.TRUE)
                    .isNotLocked(Boolean.TRUE)
                    .createdDate(new Date())
                    .build();

            this.userRepository.save(toAddUser);
        } else {
            toAddUser = user.get();
        }

        Algorithm algorithm = Algorithm.HMAC256(SecurityConstant.JWT_SECRET.getBytes());
        JwtUtils.createJWTAndAddInHeaders(algorithm, toAddUser, response);

        return toAddUser;
    }

    private boolean validateIdToken(String idToken, String provider) {
        OkHttpClient client = new OkHttpClient();

        switch (provider) {
            case "GOOGLE":
                Request request = new Request.Builder()
                        .url("https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken)
                        .build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String body = Objects.requireNonNull(response.body()).string();
                    var parsedJson = JsonParserFactory.getJsonParser().parseMap(body);
                    return !parsedJson.containsKey("error");
                } catch (IOException e) {
                    return false;
                }
            default:
                return false;
        }
    }

}