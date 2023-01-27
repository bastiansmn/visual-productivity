package com.bastiansmn.vp.label.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LabelCreationDTO {

    private String name;
    private String color;
    private String projectId;

}
