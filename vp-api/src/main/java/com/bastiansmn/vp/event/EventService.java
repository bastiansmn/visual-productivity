package com.bastiansmn.vp.event;

import com.bastiansmn.vp.event.dto.EventCreationDto;
import com.bastiansmn.vp.event.dto.EventUpdateDto;
import com.bastiansmn.vp.exception.FunctionalException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventService {

    EventDAO fetchById(Long event_id) throws FunctionalException;

    EventDAO create(EventCreationDto event) throws FunctionalException;

    EventDAO updateDates(EventUpdateDto event);

    EventDAO participate(Long event_id) throws FunctionalException;

    EventDAO unparticipate(Long eventId) throws FunctionalException;

    void delete(Long event_id) throws FunctionalException;

    List<EventDAO> fetchAllOfProject(String projectId, String from, String to) throws FunctionalException;

    List<EventDAO> myEvents(String from, String to) throws FunctionalException;

    Boolean isParticipating(Long eventId) throws FunctionalException;

    Boolean isParticipating(EventDAO event);

    Boolean createdByMe(Long eventId) throws FunctionalException;

    Boolean createdByMe(EventDAO event);

}
