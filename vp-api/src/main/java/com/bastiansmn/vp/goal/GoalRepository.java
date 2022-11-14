package com.bastiansmn.vp.goal;

import com.bastiansmn.vp.project.ProjectDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface GoalRepository extends JpaRepository<GoalDAO, Long> {

    Collection<GoalDAO> findAllByProject(ProjectDAO project);

}
