package com.bastiansmn.vp.task;

import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

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
    private LocalDate date_start;

    @Column(
            nullable = false
    )
    private LocalDate date_end;

    @Column(
            nullable = false
    )
    private Long duration;

    @Column(
            nullable = false
    )
    private Boolean completed;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    @JsonIgnore
    @Nullable
    private GoalDAO goal;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Set<TaskDAO> dependsOn;

    @ManyToMany(mappedBy = "dependsOn", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Set<TaskDAO> requiredBy;

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
