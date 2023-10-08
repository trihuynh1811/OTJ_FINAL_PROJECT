package com.example.FAMS.repositories;

import com.example.FAMS.dto.UserDTO;
import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.models.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<UserDTO> findUserByEmail(String email);

    List<ListUserResponse> findAllByRole(Enum role);

    <T> List<T> findBy(Class<T> classType);

}
