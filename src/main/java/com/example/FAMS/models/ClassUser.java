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
    @JoinColumn(name = "Class_ID")
    private Class ClassID;

    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User UserID;

    @Column(nullable = false)
    private String userType;
}
