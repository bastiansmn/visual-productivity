package com.bastiansmn.vp.event;

import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.project.ProjectDAO;
import lombok.*;

import javax.persistence.*;
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

    @Column(
            nullable = false
    )
    private String description;

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

    @ManyToMany(mappedBy = "events")
    private Set<GoalDAO> goals;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            updatable = false
    )
    private ProjectDAO project;


}
