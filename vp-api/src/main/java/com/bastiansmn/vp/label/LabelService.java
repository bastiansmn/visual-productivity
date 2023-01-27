package com.bastiansmn.vp.label;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.label.dto.LabelCreationDTO;

import java.util.List;
import java.util.Set;

public interface LabelService {

    LabelDAO create(LabelCreationDTO label) throws FunctionalException;

    LabelDAO fetchById(Long id) throws FunctionalException;

    void delete(Long id) throws FunctionalException;

    Set<LabelDAO> fetchAllOfProject(String projectId) throws FunctionalException;

    List<LabelDAO> filterByName(String projectId, String name) throws FunctionalException;
}
