package com.bastiansmn.vp.mail;

import com.bastiansmn.vp.user.UserDAO;
import lombok.*;

import jakarta.persistence.*;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(name = "mail_confirm")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MailConfirmDAO {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(
            nullable = false,
            unique = true,
            updatable = false
    )
    private Long confirmation_id;

    // Delete mailConfirm when user is deleted
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            updatable = false
    )
    private UserDAO concernedUser;

    @Column(nullable = false, updatable = false)
    private String confirmationCode;

    @Column(nullable = false, updatable = false)
    private Date expirationDate;

    @Column(nullable = false)
    private Integer attempts;

}
