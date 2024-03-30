package com.bastiansmn.vp.goal.dto;

import com.bastiansmn.vp.goal.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class GoalCreationDTO {

    private String name;
    private String description;
    private LocalDate date_start;
    private LocalDate deadline;
    private GoalStatus goalStatus;
    private String project_id;

}
