package com.bastiansmn.vp.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleDAO, Long> {

    Optional<RoleDAO> findByName(String roleStr);

}
