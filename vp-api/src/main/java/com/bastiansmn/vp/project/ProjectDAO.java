package com.bastiansmn.vp.project;

import com.bastiansmn.vp.event.EventDAO;
import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.label.LabelDAO;
import com.bastiansmn.vp.task.TaskDAO;
import com.bastiansmn.vp.user.UserDAO;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GenericGenerator(name = "project_id", strategy = "com.bastiansmn.vp.project.ProjectIdGenerator")
    @GeneratedValue(generator = "project_id")
    @Column(
            nullable = false,
            updatable = false
    )
    private String project_id;

    @Column(
            nullable = false
    )
    private String name;

    @Column(
            nullable = false
    )
    private String project_identifier;

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
            orphanRemoval = true
    )
    private Set<LabelDAO> allLabels;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "project",
            orphanRemoval = true
    )
    private Set<EventDAO> allEvents;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "project",
            orphanRemoval = true
    )
    private Set<GoalDAO> allGoals;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "project",
            orphanRemoval = true
    )
    private Set<TaskDAO> allTasks;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "link_user_projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
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
