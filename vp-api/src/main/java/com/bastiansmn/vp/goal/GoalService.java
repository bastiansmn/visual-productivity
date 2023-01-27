package com.bastiansmn.vp.goal;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.goal.dto.GoalCreationDTO;
import com.bastiansmn.vp.goal.dto.StatusUpdateDTO;

import java.util.Collection;

public interface GoalService {

    GoalDAO create(GoalCreationDTO goal) throws FunctionalException;

    GoalDAO fetchById(Long id) throws FunctionalException;

    Collection<GoalDAO> fetchAll(String project_id) throws FunctionalException;

    GoalDAO update(StatusUpdateDTO goal) throws FunctionalException;

    void delete(Long goalID) throws FunctionalException;

    GoalDAO assignLabel(Long goal_id, Long label_id) throws FunctionalException;

    GoalDAO unassignLabel(Long goal_id, Long label_id) throws FunctionalException;

}
