package com.bastiansmn.vp.task;

import com.bastiansmn.vp.common.Task;
import com.bastiansmn.vp.common.graph.Graph;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.task.dto.ExecutionOrder;
import com.bastiansmn.vp.task.dto.TaskCreationDTO;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface TaskService {

    TaskDAO create(TaskCreationDTO taskCreationDTO) throws FunctionalException;

    TaskDAO fetchById(Long task_id) throws FunctionalException;

    Collection<TaskDAO> fetchAllOfProject(String project_id) throws FunctionalException;

    Collection<TaskDAO> fetchAllOfGoal(Long goal_id) throws FunctionalException;

    void delete(Long task_id) throws FunctionalException;

    TaskDAO markAsDone(Long taskId) throws FunctionalException;

    TaskDAO markAsUndone(Long taskId) throws FunctionalException;

    ExecutionOrder optimizeTasks(List<Task> tasks, @Nullable Long parallelTasks) throws FunctionalException;

    Graph<Long> getGraph(List<Task> tasks) throws FunctionalException;
}
