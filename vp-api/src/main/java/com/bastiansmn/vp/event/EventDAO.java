package com.bastiansmn.vp.event;

import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.user.UserDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Table(name = "events")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EventDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            nullable = false,
            updatable = false
    )
    private Long event_id;

    @Column(
            nullable = false
    )
    private String name;

    @Column
    private String description;

    @Column
    private String videoCallLink;

    @Column(
            nullable = false
    )
    private Date date_start;

    @Column(
            nullable = false
    )
    private Date date_end;

    @Column(
            nullable = false
    )
    private Boolean whole_day;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "link_event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserDAO> participants;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            updatable = false
    )
    @JsonIgnore
    private ProjectDAO project;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            updatable = false
    )
    @JsonIgnore
    private UserDAO createdBy;


}
