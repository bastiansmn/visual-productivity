package com.bastiansmn.vp.project.impl;

import com.bastiansmn.vp.config.properties.CorsProperties;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.exception.TechnicalException;
import com.bastiansmn.vp.exception.TechnicalRule;
import com.bastiansmn.vp.pendingUserInvites.PendingInvitesCreationDTO;
import com.bastiansmn.vp.pendingUserInvites.PendingInvitesDAO;
import com.bastiansmn.vp.pendingUserInvites.PendingInvitesRepository;
import com.bastiansmn.vp.pendingUserInvites.PendingInvitesService;
import com.bastiansmn.vp.project.ProjectCreationDTO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.project.ProjectRepository;
import com.bastiansmn.vp.project.ProjectService;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final PendingInvitesRepository pendingInvitesRepository;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final CorsProperties corsProperties;

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
                .projectIdentifier(projectDTO.getName().toLowerCase().replaceAll(" ", "-"))
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

        if (this.projectRepository.findByProjectIdentifier(project.getProjectIdentifier()).isPresent())
            throw new FunctionalException(
                    FunctionalRule.PROJ_0001
            );

        log.info("Creating project: {}", project);

        return this.projectRepository.save(project);
    }

    @Override
    public ProjectDAO fetchById(String id) throws FunctionalException {
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
    public void deleteById(String project_id) throws FunctionalException {
        Optional<ProjectDAO> project = this.projectRepository.findById(project_id);

        if (project.isEmpty())
            throw new FunctionalException(FunctionalRule.PROJ_0001);

        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!project.get().getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(userEmail))
            throw new FunctionalException(FunctionalRule.PROJ_0007);

        this.projectRepository.delete(project.get());
    }

    @Override
    public UserDAO addUserToProject(String project_id, String user_email)
            throws FunctionalException, TechnicalException {
        ProjectDAO project = this.fetchById(project_id);

        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!project.getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.PROJ_0007);

        if (project.getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(user_email))
            throw new FunctionalException(FunctionalRule.PROJ_0008);


        try {
            UserDAO user = this.userService.fetchByEmail(user_email);
            project.getUsers().add(user);

            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(user.getEmail());
            helper.setSubject("Visual Productivity - Vous avez été ajouté à un projet");

            Context context = new Context();
            context.setVariables(Map.of("firstName", user.getName(), "projectName", project.getName(), "url",
                    "%s/app/projects/%s/dashboard".formatted(this.corsProperties.getCurrentOrigin(), project.getProjectId())));
            helper.setText(templateEngine.process("addUserToProject", context), true);
            this.mailSender.send(message);

            project.setUpdated_at(Date.from(Instant.now()));
            this.projectRepository.save(project);

            return user;

        } catch (FunctionalException e) {
            // User not found, send him an email to create an account
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            try {
                helper.setTo(user_email);
                helper.setSubject("Visual Productivity - Vous avez été invités à rejoindre un projet");

                Context context = new Context();
                context.setVariables(Map.of("projectName", project.getName(), "url",
                        "%s/app/projects/%s/dashboard".formatted(this.corsProperties.getCurrentOrigin(), project.getProjectId())));
                helper.setText(templateEngine.process("newUserToProject", context), true);
                this.mailSender.send(message);

                this.pendingInvitesRepository.save(
                    PendingInvitesDAO.builder()
                        .email(user_email)
                        .project(project.getProjectId())
                        .build()
                );

            } catch (MessagingException e1) {
                throw new TechnicalException(TechnicalRule.MAIL_0001);
            }

            return null;
        } catch (MessagingException e) {
            log.error("Impossible d'envoyer le mail pour ajouter un utilisateur au projet: {}", project.getProjectId());
            throw new TechnicalException(
                    TechnicalRule.MAIL_0001,
                    HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }

    @Override
    public List<ProjectDAO> fetchProjectsOfUser(String email) throws FunctionalException {
        UserDAO user = this.userService.fetchByEmail(email);
        return user.getProjects();
    }
}
