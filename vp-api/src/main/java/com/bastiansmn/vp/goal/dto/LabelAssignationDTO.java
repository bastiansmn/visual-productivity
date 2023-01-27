package com.bastiansmn.vp.goal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LabelAssignationDTO {

    private Long label_id;
    private Long goal_id;

}
