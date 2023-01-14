package com.bastiansmn.vp.pendingUserInvites;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "pending_user_invites")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PendingInvitesDAO {

    @Id
    private String email;
    private String project;

}
