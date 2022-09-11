package com.bastiansmn.vp.authorities;

import com.bastiansmn.vp.authorities.dto.AuthoritiesCreationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
public class AuthoritiesService {

    private final AuthoritiesRepository authoritiesRepository;

    public AuthoritiesDAO create(AuthoritiesCreationDTO authoritiesCreationDTO) {
        AuthoritiesDAO auth = AuthoritiesDAO.builder()
                .name(authoritiesCreationDTO.getName())
                .build();
        return this.authoritiesRepository.save(auth);
    }

    public Optional<AuthoritiesDAO> fetchById(Long id) {
        return this.authoritiesRepository.findById(id);
    }

    public AuthoritiesDAO update(AuthoritiesCreationDTO authoritiesCreationDTO) {
        AuthoritiesDAO auth = AuthoritiesDAO.builder()
                .name(authoritiesCreationDTO.getName())
                .build();
        return this.authoritiesRepository.save(auth);
    }

    public void delete(Long id) {
        this.authoritiesRepository.deleteById(id);
    }

    public void deleteAll() {
        this.authoritiesRepository.deleteAll();
    }

    public Collection<AuthoritiesDAO> fetchAll() {
        return this.authoritiesRepository.findAll();
    }

    public Collection<AuthoritiesDAO> getDefaultAuthorities() {
        return DefaultAuthorities.DEFAULT_AUTH.stream()
                .map(this.authoritiesRepository::findByName)
                .collect(Collectors.toList());
    }

}
