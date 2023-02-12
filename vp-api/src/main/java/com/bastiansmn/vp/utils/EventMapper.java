package com.bastiansmn.vp.utils;

import com.bastiansmn.vp.event.EventDAO;
import com.bastiansmn.vp.event.dto.EventDto;

public class EventMapper {

    public static EventDto toDto(EventDAO event) {
        return EventMapper.toDto(event, false);
    }

    public static EventDto toDto(EventDAO event, Boolean participating) {
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
        return dto;
    }

}
