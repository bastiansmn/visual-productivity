package com.bastiansmn.vp.event;

import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.user.UserDAO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import java.util.List;

public interface EventRepository extends
        JpaRepository<EventDAO, Long>,
        JpaSpecificationExecutor<EventDAO> {

}
