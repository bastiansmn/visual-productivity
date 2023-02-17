package com.bastiansmn.vp.goal.impl;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.goal.GoalRepository;
import com.bastiansmn.vp.goal.GoalService;
import com.bastiansmn.vp.goal.dto.GoalCreationDTO;
import com.bastiansmn.vp.goal.dto.StatusUpdateDTO;
import com.bastiansmn.vp.label.LabelDAO;
import com.bastiansmn.vp.label.LabelRepository;
import com.bastiansmn.vp.label.LabelService;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.project.ProjectRepository;
import com.bastiansmn.vp.project.ProjectService;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GoalServiceImpl implements GoalService {

    private final ProjectService projectService;
    private final LabelService labelService;
    private final GoalRepository goalRepository;
    private final ProjectRepository projectRepository;
    private final LabelRepository labelRepository;
    private final UserService userService;

    @Override
    public GoalDAO create(GoalCreationDTO goalDTO) throws FunctionalException {
        if (goalDTO.getName() == null)
            throw new FunctionalException(FunctionalRule.GOAL_0002);

        if (goalDTO.getDescription() == null)
            throw new FunctionalException(FunctionalRule.GOAL_0003);

        if (goalDTO.getDate_start() == null)
            throw new FunctionalException(FunctionalRule.GOAL_0004);

        if (goalDTO.getDeadline() == null)
            throw new FunctionalException(FunctionalRule.GOAL_0005);

        if (goalDTO.getDate_start().after(goalDTO.getDeadline()))
            throw new FunctionalException(FunctionalRule.GOAL_0005);

        if (goalDTO.getDate_start().before(Date.from(Instant.now().minus(1, ChronoUnit.DAYS))))
            throw new FunctionalException(FunctionalRule.GOAL_0005);

        if (goalDTO.getProject_id() == null)
            throw new FunctionalException(FunctionalRule.GOAL_0004);

        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = this.userService.fetchByEmail(contextUser);
        ProjectDAO project = this.projectService.fetchById(goalDTO.getProject_id());

        if (goalDTO.getDeadline().after(project.getDeadline()))
            throw new FunctionalException(FunctionalRule.GOAL_0005);

        if (!project.getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.GOAL_0006);

        project.setUpdated_at(new Date());
        this.projectRepository.save(project);

        GoalDAO goal = GoalDAO.builder()
                .name(goalDTO.getName())
                .description(goalDTO.getDescription())
                .date_start(goalDTO.getDate_start())
                .deadline(goalDTO.getDeadline())
                .status(goalDTO.getGoalStatus())
                .labels(Set.of())
                .tasks(Set.of())
                .project(project)
                .created_at(Date.from(Instant.now()))
                .build();

        return this.goalRepository.save(goal);
    }

    @Override
    public GoalDAO fetchById(Long id) throws FunctionalException {
        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        GoalDAO goal = this.goalRepository.findById(id).orElseThrow(() -> new FunctionalException(FunctionalRule.GOAL_0001));

        if (!goal.getProject().getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.GOAL_0006);

        return goal;
    }

    @Override
    public Collection<GoalDAO> fetchAll(String project_id) throws FunctionalException {
        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = this.userService.fetchByEmail(contextUser);
        ProjectDAO project = this.projectService.fetchById(project_id);

        if (!project.getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.GOAL_0006);

        return this.goalRepository.findAllByProject(project);
    }

    @Override
    public GoalDAO update(StatusUpdateDTO goal) throws FunctionalException {
        GoalDAO goalDAO = this.fetchById(goal.getGoal_id());

        if (goal.getGoalStatus() != null)
            goalDAO.setStatus(goal.getGoalStatus());

        return this.goalRepository.save(goalDAO);
    }

    @Override
    public void delete(Long goalID) throws FunctionalException {
        if (goalID == null)
            throw new FunctionalException(FunctionalRule.GOAL_0001);

        GoalDAO goal = this.fetchById(goalID);

        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!goal.getProject().getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.GOAL_0006);

        ProjectDAO project = goal.getProject();
        project.setUpdated_at(new Date());
        this.projectRepository.save(project);

        goal.getLabels().forEach(label -> {
            label.getGoals().remove(goal);
            this.labelRepository.save(label);
        });

        this.goalRepository.delete(goal);
    }

    @Override
    public GoalDAO assignLabel(Long goal_id, Long label_id) throws FunctionalException {
        GoalDAO goal = this.fetchById(goal_id);
        LabelDAO label = this.labelService.fetchById(label_id);

        if (!Objects.equals(label.getProject().getProjectId(), goal.getProject().getProjectId()))
            throw new FunctionalException(FunctionalRule.LABEL_0002);

        label.getGoals().add(goal);
        this.labelRepository.save(label);

        goal.getLabels().add(label);
        return this.goalRepository.save(goal);
    }

    @Override
    public GoalDAO unassignLabel(Long goal_id, Long label_id) throws FunctionalException {
        GoalDAO goal = this.fetchById(goal_id);
        LabelDAO label = this.labelService.fetchById(label_id);

        label.getGoals().remove(goal);
        this.labelRepository.save(label);

        goal.getLabels().remove(label);
        return this.goalRepository.save(goal);
    }

}
