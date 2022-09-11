package com.bastiansmn.vp.authorities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

class DefaultAuthorities {
    static List<String> DEFAULT_AUTH = List.of(
            "read"
    );
}
@Table(name = "authorities")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthoritiesDAO {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(
            nullable = false,
            unique = true,
            updatable = false
    )
    private Long auth_id;

    @Column(
            nullable = false,
            updatable = false,
            unique = true
    )
    private String name;

}
