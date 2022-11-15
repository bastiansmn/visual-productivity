package com.bastiansmn.vp.user;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.user.dto.UserCreationDTO;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    UserDAO create(UserCreationDTO user) throws FunctionalException, TechnicalException;

    UserDAO fetchByEmail(String email) throws FunctionalException;

    void delete(Long user_id) throws FunctionalException;

    List<UserDAO> fetchAll();

    boolean emailExists(String email);

    boolean isEnabled(String email) throws FunctionalException;

    boolean isNotLocked(String email) throws FunctionalException;

}
