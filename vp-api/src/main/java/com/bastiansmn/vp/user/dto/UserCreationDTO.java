package com.bastiansmn.vp.user.dto;

import com.bastiansmn.vp.socialAuth.UserProvider;
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

    private UserProvider provider;

}
