package com.bastiansmn.vp.project;

import lombok.*;

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
    private Date deadline;
    private boolean complete_mode;
    private String user_email;

}
