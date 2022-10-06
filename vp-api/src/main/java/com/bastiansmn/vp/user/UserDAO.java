package com.bastiansmn.vp.user;

import com.bastiansmn.vp.role.RoleDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

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

    // TODO: add confirmation code but not in the db

    private boolean isEnabled;

    private boolean isNotLocked;

}
