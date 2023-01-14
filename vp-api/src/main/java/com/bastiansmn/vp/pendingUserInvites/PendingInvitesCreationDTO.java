package com.bastiansmn.vp.pendingUserInvites;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PendingInvitesCreationDTO {

    private String email;
    private String project;

}
