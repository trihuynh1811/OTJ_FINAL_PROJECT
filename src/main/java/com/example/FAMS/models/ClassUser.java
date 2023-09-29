package com.example.FAMS.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "ClassUser")
public class ClassUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Class_id")
    private Class classID;

    @ManyToOne
    @JoinColumn(name = "Users_id")
    private User userID;

    @Column(nullable = false)
    private String userType;
}
