package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdatePermissionRequest;
import com.example.FAMS.dto.responses.ResponseObject;
import com.example.FAMS.enums.Permission;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.UserPermissionService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.example.FAMS.enums.Permission.*;

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
        var permissionList = userPermissionDAO.findAll();
        List<UserPermission> updatedList = new ArrayList<>();
        final String[] prefixArr = {"ROLE", "SYLLABUS", "TRAINING", "CLASS", "MATERIAL", "USER"};
        for (UpdatePermissionRequest updatePermission: updateRequest) {
            for (UserPermission permission : permissionList) {
                if (permission.getRole().name().equals(updatePermission.getRoleName())) {
                    int nthChild = 0;
                    Class<?> objClass = updatePermission.getClass();
                    Field[] fields = objClass.getDeclaredFields();
                    for(Field field : fields) {
                        List<Permission> tmpUserPermission = new ArrayList<>();
                        if (nthChild > 0 && nthChild < fields.length) {
                            try {
                                Object value = field.get(updatePermission);
                                if (value.equals("FULL_ACCESS")) {
                                    tmpUserPermission = List.of(
                                            Permission.valueOf(prefixArr[nthChild] + "_DELETE"),
                                            Permission.valueOf(prefixArr[nthChild] + "_CREATE"),
                                            Permission.valueOf(prefixArr[nthChild] + "_UPDATE"),
                                            Permission.valueOf(prefixArr[nthChild] + "_READ"),
                                            Permission.valueOf(prefixArr[nthChild] + "_IMPORT")
                                    );
                                } else if (value.equals("CREATE")) {
                                   tmpUserPermission.add(Permission.valueOf(prefixArr[nthChild] + "_CREATE"));
                                } else if (value.equals("VIEW")) {
                                    tmpUserPermission.add(Permission.valueOf(prefixArr[nthChild] + "_READ"));
                                } else if (value.equals("MODIFY")) {
                                    tmpUserPermission.add(Permission.valueOf(prefixArr[nthChild] + "_UPDATE"));
                                } else if (value.equals("ACCESS_DENIED")) {
                                }
                                switch (nthChild){
                                    case 1: permission.setSyllabus(tmpUserPermission); break;
                                    case 2: permission.setTrainingProgram(tmpUserPermission); break;
                                    case 3: permission.setUserClass(tmpUserPermission); break;
                                    case 4: permission.setLearningMaterial(tmpUserPermission); break;
                                    case 5: permission.setUserManagement(tmpUserPermission); break;
                                    default: break;
                                }
                                nthChild++;
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            nthChild++;
                        }
                    }
                }
            }
        }
        var savedPermissionList = userPermissionDAO.saveAll(permissionList);
        return ResponseObject.builder()
                .status("Successful")
                .message("Permission updated")
                .payload(savedPermissionList)
                .build();
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
