package com.bastiansmn.vp.project;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.user.UserDAO;

import java.util.Collection;

public interface ProjectService {
    ProjectDAO create(ProjectCreationDTO project) throws FunctionalException;

    ProjectDAO fetchById(Long id) throws FunctionalException;

    Collection<ProjectDAO> fetchAll();

    void deleteById(Long project_id) throws FunctionalException;

    ProjectDAO addUserToProject(Long project_id, String user_email) throws FunctionalException;

}
