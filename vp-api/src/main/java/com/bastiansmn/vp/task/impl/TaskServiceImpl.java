package com.bastiansmn.vp.task.impl;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.goal.GoalService;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.project.ProjectService;
import com.bastiansmn.vp.task.TaskDAO;
import com.bastiansmn.vp.task.TaskRepository;
import com.bastiansmn.vp.task.TaskService;
import com.bastiansmn.vp.task.dto.TaskCreationDTO;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ProjectService projectService;
    private final GoalService goalService;
    private final UserService userService;

    @Override
    public TaskDAO create(TaskCreationDTO taskCreationDTO) throws FunctionalException {
        if (taskCreationDTO.getName() == null)
            throw new FunctionalException(FunctionalRule.TASK_0002);

        if (taskCreationDTO.getDescription() == null)
            throw new FunctionalException(FunctionalRule.TASK_0003);

        if (taskCreationDTO.getDate_start() == null)
            throw new FunctionalException(FunctionalRule.TASK_0004);

        if (taskCreationDTO.getDate_end() == null)
            throw new FunctionalException(FunctionalRule.TASK_0004);

        if (taskCreationDTO.getDate_start().after(taskCreationDTO.getDate_end()))
            throw new FunctionalException(FunctionalRule.TASK_0004);

        if (taskCreationDTO.getDate_start().before(Date.from(Instant.now().minus(1, ChronoUnit.DAYS))))
            throw new FunctionalException(FunctionalRule.TASK_0004);

        if (taskCreationDTO.getGoal_id() == null)
            throw new FunctionalException(FunctionalRule.TASK_0006);

        if (taskCreationDTO.getProject_id() == null)
            throw new FunctionalException(FunctionalRule.TASK_0007);

        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = this.userService.fetchByEmail(contextUser);
        ProjectDAO project = this.projectService.fetchById(taskCreationDTO.getProject_id());
        GoalDAO goal = this.goalService.fetchById(taskCreationDTO.getGoal_id());

        if (taskCreationDTO.getDate_start().before(goal.getDate_start()))
            throw new FunctionalException(FunctionalRule.TASK_0011);

        if (taskCreationDTO.getDate_end().after(goal.getDeadline()))
            throw new FunctionalException(FunctionalRule.TASK_0009);

        if (!goal.getProject().getProjectId().equals(project.getProjectId()))
            throw new FunctionalException(FunctionalRule.TASK_0008);

        if (!project.getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.TASK_0005);

        TaskDAO task = TaskDAO.builder()
                .name(taskCreationDTO.getName())
                .description(taskCreationDTO.getDescription())
                .date_start(taskCreationDTO.getDate_start())
                .date_end(taskCreationDTO.getDate_end())
                .goal(goal)
                .project(project)
                .completed(false)
                .created_at(Date.from(Instant.now()))
                .build();

        return this.taskRepository.save(task);
    }

    @Override
    public TaskDAO fetchById(Long task_id) throws FunctionalException {
        return this.taskRepository.findById(task_id).orElseThrow(() -> new FunctionalException(FunctionalRule.TASK_0001));
    }

     @Override
    public Collection<TaskDAO> fetchAllOfProject(String project_id) throws FunctionalException {
        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = this.userService.fetchByEmail(contextUser);
        ProjectDAO project = this.projectService.fetchById(project_id);
        if (!project.getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.TASK_0005);

        return this.taskRepository.findAllByProject(project);
    }

    @Override
    public Collection<TaskDAO> fetchAllOfGoal(Long goal_id) throws FunctionalException {
        GoalDAO goal = this.goalService.fetchById(goal_id);
        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!goal.getProject().getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.TASK_0005);

        return this.taskRepository.findAllByGoal(goal);
    }

    @Override
    public void delete(Long task_id) throws FunctionalException {
        TaskDAO task = this.fetchById(task_id);
        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!task.getProject().getUsers().stream().map(UserDAO::getEmail).collect(Collectors.toSet()).contains(contextUser))
            throw new FunctionalException(FunctionalRule.TASK_0005);

        this.taskRepository.delete(task);
    }

    @Override
    public TaskDAO markAsDone(Long taskId) throws FunctionalException {
        TaskDAO task = this.fetchById(taskId);

        task.setCompleted(true);

        return this.taskRepository.save(task);
    }

    @Override
    public TaskDAO markAsUndone(Long taskId) throws FunctionalException {
        TaskDAO task = this.fetchById(taskId);

        task.setCompleted(false);

        return this.taskRepository.save(task);
    }
}
