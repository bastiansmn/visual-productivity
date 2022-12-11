package com.bastiansmn.vp.project.impl;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.project.ProjectCreationDTO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.project.ProjectRepository;
import com.bastiansmn.vp.project.ProjectService;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserRepository;
import com.bastiansmn.vp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    @Override
    public ProjectDAO create(ProjectCreationDTO projectDTO) throws FunctionalException {

        if (projectDTO.getDeadline() == null)
            throw new FunctionalException(
                    FunctionalRule.PROJ_0002
            );

        if (projectDTO.getName() == null)
            throw new FunctionalException(
                    FunctionalRule.PROJ_0003
            );

        if (projectDTO.getDescription() == null)
            throw new FunctionalException(
                    FunctionalRule.PROJ_0004
            );

        if (projectDTO.getDeadline().before(Date.from(Instant.now())))
            throw new FunctionalException(
                    FunctionalRule.PROJ_0005
            );

        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDAO projectCreator = userService.fetchByEmail(contextUser);

        ProjectDAO project = ProjectDAO.builder()
                .name(projectDTO.getName())
                .project_identifier(projectDTO.getName().toLowerCase().replaceAll(" ", "-"))
                .description(projectDTO.getDescription())
                .deadline(projectDTO.getDeadline())
                .token(RandomStringUtils.randomAlphanumeric(8))
                .complete_mode(projectDTO.isComplete_mode())
                .created_at(Date.from(Instant.now()))
                .allLabels(Set.of())
                .allEvents(Set.of())
                .allGoals(Set.of())
                .allTasks(Set.of())
                .users(Set.of(projectCreator))
                .build();

        log.info("Creating project: {}", project);

        return this.projectRepository.save(project);
    }

    @Override
    public ProjectDAO fetchById(Long id) throws FunctionalException {
        Optional<ProjectDAO> project = this.projectRepository.findById(id);

        if (project.isEmpty())
            throw new FunctionalException(FunctionalRule.PROJ_0001);

        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!project.get().getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(userEmail))
            throw new FunctionalException(FunctionalRule.PROJ_0007);

        return project.get();
    }

    @Override
    public Collection<ProjectDAO> fetchAll() {
        return this.projectRepository.findAll();
    }

    @Override
    public void deleteById(Long project_id) throws FunctionalException {
        Optional<ProjectDAO> project = this.projectRepository.findById(project_id);

        if (project.isEmpty())
            throw new FunctionalException(FunctionalRule.PROJ_0001);

        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!project.get().getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(userEmail))
            throw new FunctionalException(FunctionalRule.PROJ_0007);

        this.projectRepository.delete(project.get());
    }

    @Override
    public ProjectDAO addUserToProject(Long project_id, String user_email) throws FunctionalException {
        ProjectDAO project = this.fetchById(project_id);

        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!project.getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.PROJ_0007);

        if (project.getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(user_email))
            throw new FunctionalException(FunctionalRule.PROJ_0008);

        UserDAO user = this.userService.fetchByEmail(user_email);
        project.getUsers().add(user);

        return this.projectRepository.save(project);
    }

    @Override
    public List<ProjectDAO> fetchProjectsOfUser(String email) throws FunctionalException {
        UserDAO user = this.userService.fetchByEmail(email);
        return user.getProjects();
    }
}
