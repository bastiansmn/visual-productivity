package com.bastiansmn.vp.project;

import com.bastiansmn.vp.event.EventDAO;
import com.bastiansmn.vp.label.LabelDAO;
import com.bastiansmn.vp.task.TaskDAO;
import com.bastiansmn.vp.user.UserDAO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Table(name = "projects")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProjectDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            nullable = false,
            updatable = false
    )
    private Long project_id;

    @Column(
            nullable = false
    )
    private String name;

    @Column(
            nullable = false
    )
    private String description;

    @Column(
            nullable = false,
            updatable = false
    )
    @ToString.Exclude
    private String token;

    @Column(
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private Date deadline;

    @Column(
            nullable = false
    )
    private Boolean complete_mode;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<LabelDAO> labels;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<EventDAO> events;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<TaskDAO> tasks;

    @ManyToMany(
            fetch = FetchType.EAGER,
            mappedBy = "projects",
            cascade = CascadeType.ALL
    )
    private Set<UserDAO> users;

    // For pagination :
    @Column(
            updatable = false,
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT now()"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

}
