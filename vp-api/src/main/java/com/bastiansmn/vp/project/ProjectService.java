package com.bastiansmn.vp.project;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.project.dto.ProjectCreationDTO;
import com.bastiansmn.vp.user.UserDAO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface ProjectService {
    ProjectDAO create(ProjectCreationDTO project) throws FunctionalException;

    ProjectDAO fetchById(String id) throws FunctionalException;

    Collection<ProjectDAO> fetchAll();

    void deleteById(String project_id) throws FunctionalException;

    UserDAO addUserToProject(String project_id, String user_email) throws FunctionalException, TechnicalException;

    List<ProjectDAO> fetchProjectsOfUser(String email) throws FunctionalException;

}
