package com.bastiansmn.vp.label.impl;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.goal.GoalRepository;
import com.bastiansmn.vp.goal.GoalService;
import com.bastiansmn.vp.label.LabelDAO;
import com.bastiansmn.vp.label.LabelRepository;
import com.bastiansmn.vp.label.LabelService;
import com.bastiansmn.vp.label.dto.LabelCreationDTO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.project.ProjectRepository;
import com.bastiansmn.vp.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

    @Override
    public LabelDAO create(LabelCreationDTO labelDTO) throws FunctionalException {
        ProjectDAO project = this.projectService.fetchById(labelDTO.getProjectId());

        LabelDAO labelToAdd = LabelDAO.builder()
                .name(labelDTO.getName())
                .color(labelDTO.getColor())
                .project(project)
                .goals(Set.of())
                .created_at(Date.from(Instant.now()))
                .build();

        LabelDAO label = this.labelRepository.save(labelToAdd);
        project.getAllLabels().add(label);
        this.projectRepository.save(project);
        return label;
    }

    @Override
    public LabelDAO fetchById(Long id) throws FunctionalException {
        Optional<LabelDAO> label = this.labelRepository.findById(id);
        if (label.isEmpty())
            throw new FunctionalException(FunctionalRule.LABEL_0001, HttpStatus.NOT_FOUND);
        return label.get();
    }

    @Override
    public void delete(Long id) throws FunctionalException {
        Optional<LabelDAO> label = this.labelRepository.findById(id);

        if (label.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.LABEL_0001,
                    HttpStatus.NOT_FOUND
            );

        this.labelRepository.delete(label.get());
    }

    @Override
    public Set<LabelDAO> fetchAllOfProject(String projectId) throws FunctionalException {
        return this.projectService.fetchById(projectId).getAllLabels();
    }

    @Override
    public List<LabelDAO> filterByName(String projectId, String name) throws FunctionalException {
        var project = this.projectService.fetchById(projectId);

        return this.labelRepository.findAllByProjectAndNameContainingIgnoreCase(project, name);
    }

}
