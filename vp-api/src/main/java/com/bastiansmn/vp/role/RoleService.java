package com.bastiansmn.vp.role;

import com.bastiansmn.vp.authorities.AuthoritiesDAO;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.role.dto.RoleCreationDTO;

import java.util.List;
import java.util.Set;

public interface RoleService {

    RoleDAO create(RoleCreationDTO user);

    RoleDAO fetchByID(Long id) throws FunctionalException;

    RoleDAO fetchByName(String name) throws FunctionalException;

    RoleDAO addAuthorityToRole(Long roleId, Long authId) throws FunctionalException;

    RoleDAO addAuthorityToRole(String roleName, String authName) throws FunctionalException;

    RoleDAO addAuthorityToRole(String roleName, String... authName) throws FunctionalException;

    void delete(Long id) throws FunctionalException;

    void deleteAll();

    Set<RoleDAO> fetchAll();

    Set<RoleDAO> getDefaultRoles();

    boolean existsByID(Long id);

}
