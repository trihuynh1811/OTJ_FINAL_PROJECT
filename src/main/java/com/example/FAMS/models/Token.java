package com.example.FAMS.models;

import com.example.FAMS.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean revoked;

    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;
}
