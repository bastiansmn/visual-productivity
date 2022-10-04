package com.bastiansmn.vp.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationDTO {

    private String email;

    private String name;

    private String lastname;

    private String password;

}
