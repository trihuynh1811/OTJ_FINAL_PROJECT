package com.example.FAMS.repositories;

import com.example.FAMS.dto.UserDTO;
import com.example.FAMS.dto.responses.ListUserResponse;
import com.example.FAMS.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Integer countAllByStatusIs(boolean status);

    @Query(value = "SELECT a.user_id as 'userId', a.name, a.email, a.phone, a.dob, a.gender, a.created_by as 'createdBy', a.modified_by as 'modifiedBy', a.is_active as 'status', b.role\n"
            + "FROM users a INNER JOIN user_permission b on a.role = b.permission_id WHERE a.is_active = 1" , nativeQuery = true)
    Page<ListUserResponse> findAllUsersBy(Pageable pageable);

    <T> List<T> findBy(Class<T> classType);

        @Query(value = "SELECT a.user_id as 'userId', a.name, a.email, a.phone, a.dob, a.gender, a.created_by as 'createdBy', a.modified_by as 'modifiedBy', a.is_active as 'status', b.role\n"
                + "FROM users A INNER JOIN user_permission B on a.role = b.permission_id WHERE A.is_active = 1", nativeQuery = true)
    List<ListUserResponse> getAllUsersWithRole();
}
