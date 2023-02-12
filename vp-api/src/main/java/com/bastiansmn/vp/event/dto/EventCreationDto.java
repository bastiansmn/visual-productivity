package com.bastiansmn.vp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreationDto {

    private String project_id;

    private String name;
    private String description;

    private String videoCallLink;
    private Date date_start;
    private Date date_end;
    private Boolean whole_day;

}
