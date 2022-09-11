package com.bastiansmn.vp.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDAO, Long> {

    Optional<UserDAO> findByEmail(String email);

    Optional<UserDAO> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
