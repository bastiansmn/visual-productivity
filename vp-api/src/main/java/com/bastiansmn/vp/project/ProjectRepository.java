package com.bastiansmn.vp.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectDAO, String> {

    Optional<ProjectDAO> findByProjectIdentifier(String projectIdentifier);

}
