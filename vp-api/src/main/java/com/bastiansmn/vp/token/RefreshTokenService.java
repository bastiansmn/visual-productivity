package com.bastiansmn.vp.token;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.user.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RefreshTokenService {

    UserDAO refresh(HttpServletRequest request, HttpServletResponse response) throws FunctionalException;

    Boolean validate(HttpServletRequest request) throws FunctionalException;

}
