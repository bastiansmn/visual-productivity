package com.bastiansmn.vp.project.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProjectCreationDTO {

    private String name;
    private String description;
    private LocalDate deadline;
    private boolean complete_mode;

}
