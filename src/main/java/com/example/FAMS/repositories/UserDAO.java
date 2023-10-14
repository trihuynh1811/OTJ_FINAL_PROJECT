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

    @Query("""
                    SELECT Us FROM User Us WHERE Us.email = :email AND Us.status = true
            """)
    Optional<User> findByEmail(String email);

    Optional<UserDTO> findUserByEmail(String email);

    List<ListUserResponse> findAllByRole(Enum role);

    <T> List<T> findBy(Class<T> classType);

    @Query(value = "SELECT u.user_id as 'userId', u.name, u.email, u.phone, u.dob, u.gender, u.created_by as 'createdBy', u.modified_by as 'modifiedBy', u.is_active as 'status', p.role\n" +
            "FROM users as u INNER JOIN user_permission as p on u.role = p.permission_id \n" +
            "WHERE u.is_active = true", nativeQuery = true)
    List<ListUserResponse> getAllUsersWithRole();
}
