package com.example.FAMS.repositories;

import com.example.FAMS.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenDAO extends JpaRepository<Token, Integer> {

    Optional<Token> findByToken(String token);
}
