package com.bastiansmn.vp.authorities;

import com.bastiansmn.vp.role.RoleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthoritiesRepository extends JpaRepository<AuthoritiesDAO, Long> {

    Optional<AuthoritiesDAO> findByName(String authStr);
}
