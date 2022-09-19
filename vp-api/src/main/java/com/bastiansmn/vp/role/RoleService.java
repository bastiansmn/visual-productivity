package com.bastiansmn.vp.role;

import com.bastiansmn.vp.authorities.AuthoritiesDAO;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.role.dto.RoleCreationDTO;

import java.util.List;

public interface RoleService {

    RoleDAO create(RoleCreationDTO user);

    RoleDAO fetchByID(Long id) throws FunctionalException;

    RoleDAO fetchByName(String name) throws FunctionalException;

    RoleDAO addAuthorityToRole(Long roleId, Long authId) throws FunctionalException;

    RoleDAO addAuthorityToRole(String roleName, String authName) throws FunctionalException;

    RoleDAO addAuthorityToRole(String roleName, String... authName) throws FunctionalException;

    void delete(Long id) throws FunctionalException;

    void deleteAll();

    List<RoleDAO> fetchAll();

    List<RoleDAO> getDefaultRoles();

    boolean existsByID(Long id);

}
