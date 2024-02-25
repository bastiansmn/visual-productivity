package com.bastiansmn.vp.task.impl;

import com.bastiansmn.vp.common.Task;
import com.bastiansmn.vp.common.graph.Edge;
import com.bastiansmn.vp.common.graph.Graph;
import com.bastiansmn.vp.common.graph.Node;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.goal.GoalService;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.project.ProjectService;
import com.bastiansmn.vp.task.TaskDAO;
import com.bastiansmn.vp.task.TaskRepository;
import com.bastiansmn.vp.task.TaskService;
import com.bastiansmn.vp.task.dto.ExecutionOrder;
import com.bastiansmn.vp.task.dto.TaskCreationDTO;
import com.bastiansmn.vp.user.UserDAO;
import com.bastiansmn.vp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

        if (taskCreationDTO.getDuration() == null
                && (taskCreationDTO.getDate_start() == null || taskCreationDTO.getDate_end() == null)) {
            throw new FunctionalException(FunctionalRule.TASK_0004);
        }

        if (taskCreationDTO.getDate_start().isAfter(taskCreationDTO.getDate_end()))
            throw new FunctionalException(FunctionalRule.TASK_0004);

        if (taskCreationDTO.getDate_start().isBefore(LocalDate.now().minusDays(1)))
            throw new FunctionalException(FunctionalRule.TASK_0004);

        if (taskCreationDTO.getProject_id() == null)
            throw new FunctionalException(FunctionalRule.TASK_0007);

        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = this.userService.fetchByEmail(contextUser);
        ProjectDAO project = this.projectService.fetchById(taskCreationDTO.getProject_id());
        GoalDAO goal = null;
        if (taskCreationDTO.getGoal_id() != null) {
            goal = this.goalService.fetchById(taskCreationDTO.getGoal_id());

            if (taskCreationDTO.getDate_start().isBefore(goal.getDate_start()))
                throw new FunctionalException(FunctionalRule.TASK_0011);

            if (taskCreationDTO.getDate_end().isAfter(goal.getDeadline()))
                throw new FunctionalException(FunctionalRule.TASK_0009);

            if (!goal.getProject().getProjectId().equals(project.getProjectId()))
                throw new FunctionalException(FunctionalRule.TASK_0008);
        }

        if (!project.getUsers().contains(user))
            throw new FunctionalException(FunctionalRule.TASK_0005);

        Long duration = taskCreationDTO.getDuration() == null
                ? ChronoUnit.DAYS.between(taskCreationDTO.getDate_start(), taskCreationDTO.getDate_end())
                : taskCreationDTO.getDuration();


        TaskDAO task = TaskDAO.builder()
                .name(taskCreationDTO.getName())
                .description(taskCreationDTO.getDescription())
                .date_start(taskCreationDTO.getDate_start())
                .date_end(taskCreationDTO.getDate_end())
                .duration(duration)
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
        if (!project.getUsers().contains(user))
            throw new FunctionalException(FunctionalRule.TASK_0005);

        return this.taskRepository.findAllByProject(project);
    }

    @Override
    public Collection<TaskDAO> fetchAllOfGoal(Long goal_id) throws FunctionalException {
        GoalDAO goal = this.goalService.fetchById(goal_id);
        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDAO user = this.userService.fetchByEmail(contextUser);
        if (!goal.getProject().getUsers().contains(user))
            throw new FunctionalException(FunctionalRule.TASK_0005);

        return this.taskRepository.findAllByGoal(goal);
    }

    @Override
    public void delete(Long task_id) throws FunctionalException {
        TaskDAO task = this.fetchById(task_id);
        String contextUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDAO user = this.userService.fetchByEmail(contextUser);
        if (!task.getProject().getUsers().contains(user))
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

    @Override
    public ExecutionOrder optimizeTasks(List<Task> tasks, @Nullable Long parallelTasks) {
        Graph<Long> graph = this.getGraph(tasks);

        if (parallelTasks == null) parallelTasks = Long.MAX_VALUE;

        // Adapt graph to the algorithm (spread weight of the nodes to the outgoing edges)
        graph.getAdjacency().forEach((node, nodes) -> nodes.forEach(n -> n.setValue(node.getValue())));

        // Copy to a new graph to keep the old one clean.
        Graph<Long> graphCopy = new Graph<>(graph);
        List<Task> taskList = new LinkedList<>();

        // Algorithm reference :
        // https://www.youtube.com/watch?v=zUeSavd-qUk&t=639s
        List<Node<Long>> sources;
        do {
            sources = graphCopy.getAllSources();

            sources
                    .stream()
                    .limit(parallelTasks)
                    .forEach(s -> {
                        List<Task> prec = new LinkedList<>();
                        var precTask = new Object() {
                            Long maxStart = -1L;
                            Task maxTask = null;
                        };

                        // Get all prec taskList
                        graph.getAdjacency().forEach((key, value) -> {
                            if (value.contains(s)) {
                                Task task = taskList.stream()
                                        .filter(t -> t.uuid().equals(key.getRandomUUID())).findFirst()
                                        .orElseThrow(() -> new RuntimeException("Task not found"));

                                if (task.start() > precTask.maxStart) {
                                    precTask.maxStart = task.start();
                                    precTask.maxTask = task;
                                }

                                prec.add(task);
                            }
                        });

                        taskList.add(new Task(
                                s.getRandomUUID(),
                                precTask.maxTask == null ? 0L : precTask.maxTask.start() + precTask.maxTask.duration(),
                                s.getValue(),
                                prec
                        ));

                        graphCopy.getAdjacency().remove(s);
                    });
        } while (!sources.isEmpty());

        return new ExecutionOrder(
                taskList.stream().map(t -> t.start()+t.duration()).max(Long::compareTo).orElse(0L),
                taskList
        );
    }

    @Override
    public Graph<Long> getGraph(List<Task> tasks) {
        List<Node<Long>> nodes = tasks.stream().map(t -> new Node<>(t.uuid(), t.duration())).toList();
        List<Edge<Long>> edges = new LinkedList<>();

        tasks.forEach(t -> {
            t.dependencies().forEach(d -> {
                edges.add(new Edge<>(
                        nodes.stream().filter(n -> n.getRandomUUID().equals(d.uuid())).findFirst().orElseThrow(),
                        nodes.stream().filter(n -> n.getRandomUUID().equals(t.uuid())).findFirst().orElseThrow(),
                        null
                ));
            });
        });

        return new Graph<>(edges, true);
    }
}
