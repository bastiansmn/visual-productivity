package com.bastiansmn.vp.goal.impl;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.goal.GoalRepository;
import com.bastiansmn.vp.goal.GoalService;
import com.bastiansmn.vp.goal.GoalStatus;
import com.bastiansmn.vp.goal.dto.GoalCreationDTO;
import com.bastiansmn.vp.goal.dto.StatusUpdateDTO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.project.ProjectRepository;
import com.bastiansmn.vp.project.ProjectService;
import com.bastiansmn.vp.user.UserDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

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

        ProjectDAO project = this.projectService.fetchById(goalDTO.getProject_id());

        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
                .events(Set.of())
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
        ProjectDAO project = this.projectService.fetchById(project_id);

        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

        this.goalRepository.delete(goal);
    }

}
