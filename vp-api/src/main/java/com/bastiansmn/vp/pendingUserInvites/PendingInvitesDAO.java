package com.bastiansmn.vp.pendingUserInvites;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
