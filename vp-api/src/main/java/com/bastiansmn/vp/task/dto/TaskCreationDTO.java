package com.bastiansmn.vp.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreationDTO {

    private String name;
    private String description;
    private LocalDate date_start;
    private LocalDate date_end;
    private Long goal_id;
    private String project_id;

}
