package com.bastiansmn.vp.role;

import com.bastiansmn.vp.authorities.AuthoritiesDAO;
import com.bastiansmn.vp.authorities.AuthoritiesRepository;
import com.bastiansmn.vp.authorities.dto.AuthoritiesCreationDTO;
import com.bastiansmn.vp.role.dto.RoleCreationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleDAO create(RoleCreationDTO roleCreationDTO) {
        RoleDAO auth = RoleDAO.builder()
                .name(roleCreationDTO.getName())
                .build();
        return this.roleRepository.save(auth);
    }

    public Optional<RoleDAO> fetchById(Long id) {
        return this.roleRepository.findById(id);
    }

    public RoleDAO update(RoleCreationDTO roleCreationDTO) {
        RoleDAO auth = RoleDAO.builder()
                .name(roleCreationDTO.getName())
                .build();
        return this.roleRepository.save(auth);
    }

    public void delete(Long id) {
        this.roleRepository.deleteById(id);
    }

    public void deleteAll() {
        this.roleRepository.deleteAll();
    }

    public Collection<RoleDAO> fetchAll() {
        return this.roleRepository.findAll();
    }

    public Collection<RoleDAO> getDefaultRoles() {
        return DefaultRoles.DEFAULT_ROLES.stream()
                .map(this.roleRepository::findByName)
                .collect(Collectors.toList());
    }
}