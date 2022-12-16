package com.bastiansmn.vp.goal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class GoalCreationDTO {

    private String name;
    private String description;
    private Date date_start;
    private Date deadline;
    private String project_id;

}
