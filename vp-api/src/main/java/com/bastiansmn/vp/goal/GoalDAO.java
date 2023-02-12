package com.bastiansmn.vp.goal;

import com.bastiansmn.vp.event.EventDAO;
import com.bastiansmn.vp.label.LabelDAO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.bastiansmn.vp.task.TaskDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table(name = "goals")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GoalDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            nullable = false,
            updatable = false
    )
    private Long goal_id;

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
    private Date deadline;

    @Column(
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private GoalStatus status;

    @ManyToMany(
            fetch = FetchType.EAGER,
            mappedBy = "goals"
    )
    private Set<LabelDAO> labels;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "goal",
            orphanRemoval = true
    )
    private Set<TaskDAO> tasks;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "project_id"
    )
    @JsonIgnore
    private ProjectDAO project;

    // For pagination :
    @Column(
            updatable = false,
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT now()"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

}
