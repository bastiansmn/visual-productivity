package com.bastiansmn.vp.authorities;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

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
    @GeneratedValue(strategy = IDENTITY)
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
