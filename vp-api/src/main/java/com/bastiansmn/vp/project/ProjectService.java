package com.bastiansmn.vp.project;

import com.bastiansmn.vp.exception.FunctionalException;

import java.util.Collection;

public interface ProjectService {
    ProjectDAO create(ProjectCreationDTO project);

    ProjectDAO fetchById(Long id) throws FunctionalException;

    Collection<ProjectDAO> fetchAll();

    void deleteById(Long project_id) throws FunctionalException;
}
