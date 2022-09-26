package com.bastiansmn.vp.role;

import com.bastiansmn.vp.authorities.AuthoritiesDAO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;

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
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(name = "link_role_auths",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_id"))
    @ToString.Exclude
    private List<AuthoritiesDAO> authorities;

}
