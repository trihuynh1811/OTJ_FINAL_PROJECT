package com.example.FAMS.models;

import com.example.FAMS.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "UserPermission")
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "role",nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "syllabus",nullable = false)
    private String syllabus;

    @Column(name = "user_class",nullable = false)
    private String userClass;

    @Column(name = "learning_material",nullable = false)
    private String learningMaterial;

    @OneToMany(mappedBy = "role")
    private final Set<User> users = new HashSet<>();
}
