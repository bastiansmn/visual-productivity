package com.bastiansmn.vp.goal.dto;

import com.bastiansmn.vp.goal.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDTO {

    private Long goal_id;
    private GoalStatus goalStatus;

}
