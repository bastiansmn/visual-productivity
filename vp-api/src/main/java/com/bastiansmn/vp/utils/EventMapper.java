package com.bastiansmn.vp.utils;

import com.bastiansmn.vp.event.EventDAO;
import com.bastiansmn.vp.event.dto.EventDto;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.user.UserDAO;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class EventMapper {

    public static EventDto toDto(EventDAO event) {
        return EventMapper.toDto(event, false, false);
    }

    public static EventDto toDto(EventDAO event, Boolean participating, Boolean createdByMe) {
        EventDto dto = new EventDto();
        dto.setEvent_id(event.getEvent_id());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setVideoCallLink(event.getVideoCallLink());
        dto.setDate_start(event.getDate_start());
        dto.setDate_end(event.getDate_end());
        dto.setWhole_day(event.getWhole_day());
        dto.setParticipants(event.getParticipants());
        dto.setParticipating(participating);
        dto.setCreatedByMe(createdByMe);
        return dto;
    }

    public static EventDAO toDao(EventDto event, ProjectDAO project, UserDAO user) {
        EventDAO dao = new EventDAO();
        dao.setEvent_id(event.getEvent_id());
        dao.setName(event.getName());
        dao.setDescription(event.getDescription());
        dao.setVideoCallLink(event.getVideoCallLink());
        dao.setDate_start(event.getDate_start());
        dao.setDate_end(event.getDate_end());
        dao.setWhole_day(event.getWhole_day());
        dao.setParticipants(event.getParticipants());
        dao.setProject(project);
        dao.setCreatedBy(user);
        return dao;
    }

    public static Set<EventDto> toDtoSet(Collection<EventDAO> events) {
        return events.stream().map(EventMapper::toDto).collect(Collectors.toSet());
    }

    public static Set<EventDAO> toDaoSet(Set<EventDto> allEvents, ProjectDAO project, UserDAO user) {
        return allEvents.stream().map(e ->
            EventMapper.toDao(e, project, user)
        ).collect(Collectors.toSet());
    }
}
