package com.bastiansmn.vp.project;

import com.bastiansmn.vp.event.EventDAO;
import com.bastiansmn.vp.goal.GoalDAO;
import com.bastiansmn.vp.label.LabelDAO;
import com.bastiansmn.vp.task.TaskDAO;
import com.bastiansmn.vp.user.UserDAO;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

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
    @UuidGenerator
    @Column(
            name = "project_id",
            nullable = false,
            updatable = false
    )
    private UUID projectId;

    @Column(
            nullable = false
    )
    private String name;

    @Column(
            name = "project_identifier",
            nullable = false,
            unique = true
    )
    private String projectIdentifier;

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
    private LocalDate deadline;

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
    private LocalDateTime created_at;

    @Column(
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT now()"
    )
    private LocalDateTime updated_at;

    @PrePersist
    protected void onCreate() {
        updated_at = created_at = LocalDateTime.now();
    }
}
