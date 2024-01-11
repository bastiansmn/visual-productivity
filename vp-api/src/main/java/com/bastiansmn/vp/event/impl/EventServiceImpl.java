package com.bastiansmn.vp.event.impl;

import com.bastiansmn.vp.event.EventDAO;
import com.bastiansmn.vp.event.EventRepository;
import com.bastiansmn.vp.event.EventService;
import com.bastiansmn.vp.event.EventSpecification;
import com.bastiansmn.vp.event.dto.EventCreationDto;
import com.bastiansmn.vp.event.dto.EventDto;
import com.bastiansmn.vp.event.dto.EventUpdateDto;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.exception.FunctionalRule;
import com.bastiansmn.vp.goal.GoalRepository;
import com.bastiansmn.vp.goal.GoalService;
import com.bastiansmn.vp.project.ProjectService;
import com.bastiansmn.vp.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserService userService;
    private final GoalService goalService;
    private final ProjectService projectService;
    private final EventRepository eventRepository;
    private final GoalRepository goalRepository;
    private final EventSpecification eventSpecification;

    @Override
    public EventDAO fetchById(Long event_id) throws FunctionalException {
        var event = this.eventRepository.findById(event_id);

        if (event.isEmpty())
            throw new FunctionalException(
                    FunctionalRule.EVENT_0001
            );

        return event.get();
    }

    @Override
    public EventDAO create(EventCreationDto eventDto) throws FunctionalException {
        String userContext = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = this.userService.fetchByEmail(userContext);
        var project = this.projectService.fetchById(eventDto.getProject_id());

        if (project.getUsers().stream().noneMatch(u -> u.getEmail().equals(userContext)))
            throw new FunctionalException(
                    FunctionalRule.EVENT_0002,
                    HttpStatus.UNAUTHORIZED
            );

        var event = EventDAO.builder()
                .name(eventDto.getName())
                .description(eventDto.getDescription())
                .date_start(eventDto.getDate_start())
                .date_end(eventDto.getDate_end())
                .whole_day(eventDto.getWhole_day())
                .videoCallLink(eventDto.getVideoCallLink())
                .participants(Set.of(user))
                .project(project)
                .createdBy(user)
                .build();

        event = this.eventRepository.save(event);
        return event;
    }

    @Override
    public EventDAO updateDates(EventUpdateDto eventDto) {
        return null;
    }

    @Override
    public EventDAO participate(Long event_id) throws FunctionalException {
        var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var event = this.fetchById(event_id);
        var user = this.userService.fetchByEmail(email);

        if (event.getProject().getUsers().stream().noneMatch(u -> u.getEmail().equals(email)))
            throw new FunctionalException(
                    FunctionalRule.EVENT_0002,
                    HttpStatus.UNAUTHORIZED
            );

        if (event.getParticipants().stream().anyMatch(u -> u.getEmail().equals(email)))
            throw new FunctionalException(
                    FunctionalRule.EVENT_0003
            );

        event.getParticipants().add(user);
        return this.eventRepository.save(event);
    }

    @Override
    public EventDAO unparticipate(Long eventId) throws FunctionalException {
        var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var event = this.fetchById(eventId);
        var user = this.userService.fetchByEmail(email);

        if (event.getProject().getUsers().stream().noneMatch(u -> u.getEmail().equals(email)))
            throw new FunctionalException(
                    FunctionalRule.EVENT_0002,
                    HttpStatus.UNAUTHORIZED
            );

        if (event.getParticipants().stream().noneMatch(u -> u.getEmail().equals(email)))
            throw new FunctionalException(
                    FunctionalRule.EVENT_0004
            );

        event.getParticipants().remove(user);
        return this.eventRepository.save(event);
    }

    @Override
    public void delete(Long event_id) throws FunctionalException {
        this.eventRepository.delete(this.fetchById(event_id));
    }

    @Override
    public List<EventDAO> fetchAllOfProject(String projectId, String from, String to) throws FunctionalException {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var project = this.projectService.fetchById(projectId);

        if (project.getUsers().stream().noneMatch(u -> u.getEmail().equals(email)))
            throw new FunctionalException(
                    FunctionalRule.EVENT_0002,
                    HttpStatus.UNAUTHORIZED
            );

        var dateParsedFrom = LocalDateTime.parse(from);
        var dateParsedTo = LocalDateTime.parse(to);

        return this.eventRepository.findAll(
                eventSpecification.getFilter(
                        Map.of(
                                "project_id", projectId,
                                "from", dateParsedFrom.toString(),
                                "to", dateParsedTo.toString()
                        )
                )
        );
    }

    @Override
    public List<EventDAO> myEvents(String from, String to) throws FunctionalException {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = this.userService.fetchByEmail(email);

        var map = new HashMap<String, String>();
        map.put("user_id", user.getUser_id().toString());

        if (from != null)
            map.put("from", from);

        if (to != null)
            map.put("to", to);

        return this.eventRepository.findAll(
                eventSpecification.getFilter(map)
        );
    }

    @Override
    public Boolean isParticipating(Long eventId) throws FunctionalException {
        var event = this.fetchById(eventId);

        return this.isParticipating(event);
    }

    @Override
    public Boolean isParticipating(EventDAO event) {
        var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return event.getParticipants().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    @Override
    public Boolean createdByMe(Long eventId) throws FunctionalException {
        var event = this.fetchById(eventId);

        return this.createdByMe(event);
    }

    @Override
    public Boolean createdByMe(EventDAO event) {
        var email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return event.getCreatedBy().getEmail().equals(email);
    }

    @Override
    public EventDAO update(EventDto event) throws FunctionalException {
        EventDAO eventDAO = eventRepository.findById(event.getEvent_id())
                .orElseThrow(() -> new FunctionalException(
                        FunctionalRule.EVENT_0001
                ));

        eventDAO.setName(event.getName());
        eventDAO.setDescription(event.getDescription());
        eventDAO.setVideoCallLink(event.getVideoCallLink());
        eventDAO.setDate_start(event.getDate_start());
        eventDAO.setDate_end(event.getDate_end());
        eventDAO.setWhole_day(event.getWhole_day());

        return eventRepository.save(eventDAO);
    }
}
