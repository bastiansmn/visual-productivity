package com.bastiansmn.vp.project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectDAO, String> {
}
