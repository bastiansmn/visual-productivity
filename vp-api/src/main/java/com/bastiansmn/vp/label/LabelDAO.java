package com.bastiansmn.vp.label;

import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.project.ProjectDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.awt.*;
import java.util.Date;
import java.util.Set;

@Table(name = "labels")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LabelDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            nullable = false,
            updatable = false
    )
    private Long label_id;

    @Column(
            nullable = false,
            updatable = false
    )
    private String name;

    @Column(
            nullable = false,
            updatable = false
    )
    private String color;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            updatable = false
    )
    @JsonIgnore
    private ProjectDAO project;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "link_goal_labels",
            joinColumns = @JoinColumn(name = "label_id"),
            inverseJoinColumns = @JoinColumn(name = "goal_id")
    )
    @JsonIgnore
    private Set<GoalDAO> goals;

    // For pagination :
    @Column(
            name = "created_at",
            updatable = false,
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT now()"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

}
