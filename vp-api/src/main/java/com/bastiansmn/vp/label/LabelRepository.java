package com.bastiansmn.vp.label;

import com.bastiansmn.vp.project.ProjectDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabelRepository extends JpaRepository<LabelDAO, Long> {

    List<LabelDAO> findAllByProjectAndNameContainingIgnoreCase(ProjectDAO project, String name);

}
