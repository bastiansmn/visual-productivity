package com.bastiansmn.vp.role;

import lombok.*;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

class DefaultRoles {
    static List<String> DEFAULT_ROLES = List.of(
            "ROLE_USER"
    );
}

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
    @GeneratedValue(strategy = AUTO)
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

}
