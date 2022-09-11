package com.bastiansmn.vp.user;

import com.bastiansmn.vp.authorities.AuthoritiesDAO;
import com.bastiansmn.vp.role.RoleDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.GenerationType.AUTO;

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
    @GeneratedValue(strategy = AUTO)
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

    @Column(
            unique = true,
            nullable = false
    )
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(name = "link_user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private List<RoleDAO> roles;

    @ManyToMany
    @JoinTable(name = "link_user_auths",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_id"))
    @ToString.Exclude
    private List<AuthoritiesDAO> authorities;

    private boolean isEnabled;

    private boolean isNotLocked;

}
