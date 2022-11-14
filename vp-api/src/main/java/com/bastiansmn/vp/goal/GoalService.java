package com.bastiansmn.vp.goal;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.goal.dto.GoalCreationDTO;

import java.util.Collection;

public interface GoalService {

    GoalDAO create(GoalCreationDTO goal) throws FunctionalException;

    GoalDAO fetchById(Long id) throws FunctionalException;

    Collection<GoalDAO> fetchAll(Long project_id) throws FunctionalException;

    void delete(Long goalID) throws FunctionalException;

}
