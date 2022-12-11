package com.bastiansmn.vp.label;

import com.bastiansmn.vp.project.ProjectDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

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
