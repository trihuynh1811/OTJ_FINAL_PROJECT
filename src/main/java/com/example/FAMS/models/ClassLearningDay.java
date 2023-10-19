package com.example.FAMS.models;

import jakarta.persistence.*;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "classLearningDay")
public class ClassLearningDay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "enroll_date", nullable = false)
    private Date enrollDate;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classes;


}
