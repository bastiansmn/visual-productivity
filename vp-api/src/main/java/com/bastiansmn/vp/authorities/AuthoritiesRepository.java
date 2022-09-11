package com.bastiansmn.vp.authorities;

import com.bastiansmn.vp.role.RoleDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepository extends JpaRepository<AuthoritiesDAO, Long> {

    AuthoritiesDAO findByName(String authStr);
}
