package com.bastiansmn.vp.role;

import com.bastiansmn.vp.authorities.AuthoritiesDAO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Table(name = "role")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RoleDAO {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(
            nullable = false,
            unique = true,
            updatable = false
    )
    private Long role_id;

    @Column(
            nullable = false,
            updatable = false,
            unique = true
    )
    private String name;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(name = "link_role_auths",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_id"))
    @ToString.Exclude
    private Set<AuthoritiesDAO> authorities;

}
