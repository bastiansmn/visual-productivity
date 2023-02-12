package com.bastiansmn.vp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateDto {

    private Long eventId;
    private Date dateStart;
    private Date dateEnd;
}
