package com.bastiansmn.vp.task;

import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tasks")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TaskDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long task_id;

    @Column(
            nullable = false
    )
    private String name;

    private String description;

    @Column(
            nullable = false
    )
    private Date date_start;

    @Column(
            nullable = false
    )
    private Date date_end;

    @ManyToOne
    @JoinColumn(
            name = "goal_id",
            nullable = false,
            updatable = false
    )
    @JsonIgnore
    private GoalDAO goal;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            updatable = false
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
