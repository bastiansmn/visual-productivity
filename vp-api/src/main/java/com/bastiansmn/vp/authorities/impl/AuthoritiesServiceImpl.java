package com.bastiansmn.vp.authorities.impl;

import com.bastiansmn.vp.authorities.AuthoritiesDAO;
import com.bastiansmn.vp.authorities.AuthoritiesRepository;
import com.bastiansmn.vp.authorities.AuthoritiesService;
import com.bastiansmn.vp.authorities.dto.AuthoritiesCreationDTO;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthoritiesServiceImpl implements AuthoritiesService {

    private final AuthoritiesRepository authoritiesRepository;

    public AuthoritiesDAO create(AuthoritiesCreationDTO authoritiesCreationDTO) {
        AuthoritiesDAO role = AuthoritiesDAO.builder()
                .name(authoritiesCreationDTO.getName())
                .build();

        log.info("Creating authority {}", role);
        return this.authoritiesRepository.save(role);
    }

    public AuthoritiesDAO fetchByID(Long id) throws FunctionalException {
        Optional<AuthoritiesDAO> role = this.authoritiesRepository.findById(id);

        if (role.isEmpty())
            throw new FunctionalException(FunctionalRule.AUTH_0001, HttpStatus.NOT_FOUND);

        log.info("Fetching auth with id {}", id);
        return role.get();
    }

    public AuthoritiesDAO fetchByName(String name) throws FunctionalException {
        Optional<AuthoritiesDAO> role = this.authoritiesRepository.findByName(name);

        if (role.isEmpty())
            throw new FunctionalException(FunctionalRule.ROLE_0001, HttpStatus.NOT_FOUND);

        log.info("Fetching auth with name {}", name);
        return role.get();
    }

    public void delete(Long id) throws FunctionalException {
        Optional<AuthoritiesDAO> role = this.authoritiesRepository.findById(id);
        if (role.isEmpty())
            throw new FunctionalException(FunctionalRule.AUTH_0001, HttpStatus.NOT_FOUND);

        log.info("Deleting role with id {}", id);
        this.authoritiesRepository.deleteById(id);
    }

    public void deleteAll() {
        log.info("Deleting all authorities");
        this.authoritiesRepository.deleteAll();
    }

    public List<AuthoritiesDAO> fetchAll() {
        return this.authoritiesRepository.findAll();
    }

    public boolean existsByID(Long id) {
        return this.authoritiesRepository.existsById(id);
    }

}
