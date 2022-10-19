package com.bastiansmn.vp.socialAuth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialUserDTO {

    private String provider;

    private String id;

    private String email;

    private String name;

    private String photoUrl;

    private String firstName;

    private String lastName;

    private String authToken;

    private String idToken;

    private String authorizationCode;

    private Object response;


}
