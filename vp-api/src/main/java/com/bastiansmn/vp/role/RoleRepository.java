package com.bastiansmn.vp.role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleDAO, Long> {

    RoleDAO findByName(String roleStr);
}
