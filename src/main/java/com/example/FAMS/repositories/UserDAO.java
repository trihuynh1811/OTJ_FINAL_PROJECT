package com.example.FAMS.repositories;

import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    
    List<ListUserResponse> findAllByRole(Enum role);
}
