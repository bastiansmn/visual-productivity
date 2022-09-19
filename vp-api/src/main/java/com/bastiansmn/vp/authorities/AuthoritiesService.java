package com.bastiansmn.vp.authorities;

import com.bastiansmn.vp.authorities.dto.AuthoritiesCreationDTO;
import com.bastiansmn.vp.exception.FunctionalException;

import java.util.List;

public interface AuthoritiesService {

    AuthoritiesDAO create(AuthoritiesCreationDTO user);

    AuthoritiesDAO fetchByID(Long id) throws FunctionalException;

    AuthoritiesDAO fetchByName(String name) throws FunctionalException;

    void delete(Long id) throws FunctionalException;

    void deleteAll();

    List<AuthoritiesDAO> fetchAll();

    boolean existsByID(Long id);

}
