package com.bastiansmn.vp.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModificationDTO {

    private Long id;

    private String name;

    private String lastname;

    private String email;

}
