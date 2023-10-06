package com.example.FAMS.models;


import com.example.FAMS.enums.Permission;
import com.example.FAMS.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "dob", nullable = false)
    private Date dob;

    @Column(name = "gender", nullable = false)
    private String gender;

    @ManyToOne
    @JoinColumn(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserPermission role;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @Column(name = "modified_date", nullable = false)
    private Date modifiedDate;

    @OneToMany(mappedBy = "userID", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ClassUser> classUsers = new HashSet<>();

    @OneToMany(mappedBy = "userID")
    @JsonManagedReference
    private final Set<TrainingProgram> trainingPrograms = new HashSet<>();

    @OneToMany(mappedBy = "userID")
    @JsonManagedReference
    private final Set<Syllabus> syllabusList = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private final Set<Token> tokens = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
