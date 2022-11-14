package com.bastiansmn.vp.task;

import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.project.ProjectDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TaskRepository extends JpaRepository<TaskDAO, Long> {

    Collection<TaskDAO> findAllByProject(ProjectDAO project);

    Collection<TaskDAO> findAllByGoal(GoalDAO goal);

}
