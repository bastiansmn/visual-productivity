package com.bastiansmn.vp.project.impl;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.project.ProjectCreationDTO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.project.ProjectRepository;
import com.bastiansmn.vp.project.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectDAO create(ProjectCreationDTO project) {
        System.out.println(project);
        ProjectDAO toAddProject = ProjectDAO.builder()
                .name(project.getName())
                .description(project.getDescription())
                .deadline(project.getDeadline())
                .token(RandomStringUtils.randomAlphanumeric(8))
                .complete_mode(project.isComplete_mode())
                .created_at(Date.from(Instant.now()))
                .labels(Set.of())
                .events(Set.of())
                .tasks(Set.of())
                .build();

        return this.projectRepository.save(toAddProject);
    }

    @Override
    public ProjectDAO fetchById(Long id) throws FunctionalException {
        // TODO Vérifier si l'user est dans le projet
        if (this.projectRepository.findById(id).isEmpty())
            throw new FunctionalException(FunctionalRule.PROJ_0001);

        return this.projectRepository.findById(id).get();
    }

    @Override
    public Collection<ProjectDAO> fetchAll() {
        return this.projectRepository.findAll();
    }

    @Override
    public void deleteById(Long project_id) throws FunctionalException {
        // TODO Vérifier si l'user est dans le projet et y est admin
        if (this.projectRepository.findById(project_id).isEmpty())
            throw new FunctionalException(FunctionalRule.PROJ_0001);

        this.projectRepository.deleteById(project_id);
    }
}
