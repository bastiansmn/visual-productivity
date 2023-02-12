package com.bastiansmn.vp.event;

import com.bastiansmn.vp.event.dto.EventCreationDto;
import com.bastiansmn.vp.event.dto.EventUpdateDto;
import com.bastiansmn.vp.exception.FunctionalException;

import java.util.List;

public interface EventService {

    EventDAO fetchById(Long event_id) throws FunctionalException;

    EventDAO create(EventCreationDto event) throws FunctionalException;

    EventDAO updateDates(EventUpdateDto event);

    EventDAO participate(Long event_id) throws FunctionalException;

    EventDAO unparticipate(Long eventId) throws FunctionalException;

    void delete(Long event_id) throws FunctionalException;

    List<EventDAO> fetchAllOfProject(String projectId) throws FunctionalException;

    List<EventDAO> myEvents() throws FunctionalException;

    Boolean isParticipating(Long eventId) throws FunctionalException;

    Boolean isParticipating(EventDAO event);

}
