package com.bastiansmn.vp.event.dto;

import com.bastiansmn.vp.user.UserDAO;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class EventDto {

    private Long event_id;
    private String name;
    private String description;
    private String videoCallLink;
    private Date date_start;
    private Date date_end;
    private Boolean whole_day;
    private Set<UserDAO> participants;
    private Boolean participating;
    private Boolean createdByMe;

}
