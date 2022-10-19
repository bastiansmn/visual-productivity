package com.bastiansmn.vp.socialAuth;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.socialAuth.dto.SocialUserDTO;
import com.bastiansmn.vp.user.UserDAO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SocialOAuth2Service {

    UserDAO login(SocialUserDTO userDTO, HttpServletResponse response) throws FunctionalException;

}
