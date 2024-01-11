package com.bastiansmn.vp.user;

import com.bastiansmn.vp.event.EventDAO;
import com.bastiansmn.vp.mail.MailConfirmDAO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.socialAuth.UserProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserDAO {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(
            nullable = false,
            unique = true,
            updatable = false
    )
    private Long user_id;

    @Column(
            unique = true,
            nullable = false,
            updatable = false
    )
    private String email;

    @Column(nullable = false)
    private String name;

    private String lastname;

    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserProvider provider;

    @Column
    private String avatar;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "link_user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private Set<RoleDAO> roles;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "link_user_projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private List<ProjectDAO> projects;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "link_event_participants",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private Set<EventDAO> events;

    @OneToMany(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonIgnore
    private List<MailConfirmDAO> mailConfirmations;

    @Column(
            name = "created_date",
            updatable = false,
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT now()"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private boolean isEnabled;

    private boolean isNotLocked;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserDAO user) {
            return this.user_id.equals(user.getUser_id());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.user_id.hashCode();
    }
}
