package com.bastiansmn.vp.token;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.user.UserDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RefreshTokenService {

    UserDAO refresh(HttpServletRequest request, HttpServletResponse response) throws FunctionalException;

    Boolean validate(HttpServletRequest request, HttpServletResponse response) throws FunctionalException;

}
