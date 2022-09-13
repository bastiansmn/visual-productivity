package com.bastiansmn.vp.role.impl;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.role.DefaultRoles;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.role.RoleRepository;
import com.bastiansmn.vp.role.RoleService;
import com.bastiansmn.vp.role.dto.RoleCreationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleDAO create(RoleCreationDTO roleCreationDTO) {
        RoleDAO role = RoleDAO.builder()
                .name(roleCreationDTO.getName())
                .build();

        log.info("Creating role {}", role);
        return this.roleRepository.save(role);
    }

    public RoleDAO fetchByID(Long id) throws FunctionalException {
        Optional<RoleDAO> role = this.roleRepository.findById(id);

        if (role.isEmpty())
            throw new FunctionalException(FunctionalRule.ROLE_0001, HttpStatus.NOT_FOUND);

        log.info("Fetching role with id {}", id);
        return role.get();
    }

    public RoleDAO fetchByName(String name) throws FunctionalException {
        Optional<RoleDAO> role = this.roleRepository.findByName(name);

        if (role.isEmpty())
            throw new FunctionalException(FunctionalRule.ROLE_0001, HttpStatus.NOT_FOUND);

        log.info("Fetching role with name {}", name);
        return role.get();
    }

    public void delete(Long id) throws FunctionalException {
        Optional<RoleDAO> role = this.roleRepository.findById(id);
        if (role.isEmpty())
            throw new FunctionalException(FunctionalRule.ROLE_0001, HttpStatus.NOT_FOUND);

        log.info("Deleting role with id {}", id);
        this.roleRepository.deleteById(id);
    }

    public void deleteAll() {
        log.info("Deleting all roles");
        this.roleRepository.deleteAll();
    }

    public List<RoleDAO> fetchAll() {
        return this.roleRepository.findAll();
    }

    public List<RoleDAO> getDefaultRoles() {
        return DefaultRoles.DEFAULT_ROLES.stream()
                .map(role -> {
                    Optional<RoleDAO> optRole = this.roleRepository.findByName(role);
                    if (optRole.isEmpty()) return null;
                    return optRole.get();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}