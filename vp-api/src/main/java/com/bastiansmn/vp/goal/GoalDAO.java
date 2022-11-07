package com.bastiansmn.vp.goal;

import com.bastiansmn.vp.event.EventDAO;
import com.bastiansmn.vp.label.LabelDAO;
import com.bastiansmn.vp.task.TaskDAO;
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
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "link_goal_labels",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<LabelDAO> labels;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "link_goal_events",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<EventDAO> events;

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "goal",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<TaskDAO> tasks;

    // For pagination :
    @Column(
            updatable = false,
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT now()"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

}
