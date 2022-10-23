package com.bastiansmn.vp.task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskDAO, Long> {
}
