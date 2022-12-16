package com.bastiansmn.vp.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreationDTO {

    private String name;
    private String description;
    private Date date_start;
    private Date date_end;
    private Long goal_id;
    private String project_id;

}
