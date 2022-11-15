package com.bastiansmn.vp.user;

import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.role.RoleDAO;
import com.bastiansmn.vp.socialAuth.UserProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
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
    private Set<ProjectDAO> projects;

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

}
