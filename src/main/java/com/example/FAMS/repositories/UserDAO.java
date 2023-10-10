package com.example.FAMS.repositories;

import com.example.FAMS.dto.UserDTO;
import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<UserDTO> findUserByEmail(String email);

    List<ListUserResponse> findAllByRole(Enum role);

    <T> List<T> findBy(Class<T> classType);

    @Query(value = "SELECT a.user_id as 'userId', a.name, a.email, a.phone, a.dob, a.gender ,b.role\n"
            + "FROM users A INNER JOIN user_permission B on a.role = b.permission_id", nativeQuery = true)
    List<ListUserResponse> getAllUsersWithRole();
}
