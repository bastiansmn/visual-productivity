package com.bastiansmn.vp.user;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String lastname;

    private String password;

}
