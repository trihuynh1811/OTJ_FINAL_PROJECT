package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdatePermissionRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.UserPermissionService;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {
    private final Logger logger = LoggerFactory.getLogger(UserPermissionServiceImpl.class);
    private final UserPermissionDAO userPermissionDAO;
    private final UserDAO userDAO;

    @Override
    public List<UserPermission> getUserPermission() {
        return userPermissionDAO.findAll();
    }

    @Override
    public ResponseObject updatePermission(List<UpdatePermissionRequest> updateRequest) {
        return null;
    }
    @Override
    public ResponseEntity<ResponseObject> grantPermission(int userID, Role role) {
        var person = userDAO.findById(userID).orElse(null);
        var permission = userPermissionDAO.findUserPermissionByRole(role)
                .orElse(null);
        if (person == null) {
            return null;
        }
        person.setRole(permission);

        logger.info("Grant permission for a user");
        return ResponseEntity.ok(new ResponseObject("Successful", "Change user permission", userDAO.save(person)));
    }
}
