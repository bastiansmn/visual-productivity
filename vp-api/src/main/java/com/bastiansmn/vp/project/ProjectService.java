package com.bastiansmn.vp.project;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.user.UserDAO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProjectService {
    ProjectDAO create(ProjectCreationDTO project) throws FunctionalException;

    ProjectDAO fetchById(String id) throws FunctionalException;

    Collection<ProjectDAO> fetchAll();

    void deleteById(String project_id) throws FunctionalException;

    ProjectDAO addUserToProject(String project_id, String user_email) throws FunctionalException;

    List<ProjectDAO> fetchProjectsOfUser(String email) throws FunctionalException;

}
