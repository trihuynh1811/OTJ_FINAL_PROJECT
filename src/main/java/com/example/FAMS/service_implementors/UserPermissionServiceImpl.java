package com.example.FAMS.service_implementors;

import com.example.FAMS.dto.requests.UpdatePermissionRequest;
import com.example.FAMS.dto.responses.GetUserPermissionsResponse;
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
    public List<GetUserPermissionsResponse> getUserPermission() {
        var listUserDTO = userPermissionDAO.findAll();
        List<GetUserPermissionsResponse> responseList = new ArrayList<>();
        for (UserPermission userPermission : listUserDTO) {
            GetUserPermissionsResponse responseObj = new GetUserPermissionsResponse();
            Class<?> objClass = userPermission.getClass();
            Field[] fields = objClass.getDeclaredFields();
            int countField = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(userPermission);
                    if (countField != 0) {
                        String permissionString;
                        if (countField != 1) {
                            permissionString = fieldValue.toString()
                                    .substring(1, fieldValue.toString().length() - 1);
                        } else {
                            permissionString = fieldValue.toString();
                        }
                        switch (countField) {
                            case 1:
                                responseObj.setRoleName(fieldValue.toString());
                                break;
                            case 2:
                                if (!permissionString.isEmpty()) {
                                    if (permissionString.split(",").length == 5) {
                                        responseObj.setSyllabus("FULL_ACCESS");
                                    } else if (permissionString.split(",").length == 1){
                                        responseObj.setSyllabus(permissionString.split("_")[1]);
                                    } else {
                                        responseObj.setSyllabus(permissionString);
                                    }
                                } else {
                                    responseObj.setSyllabus("ACCESS_DENIED");
                                }
                                break;
                            case 3:
                                if (!permissionString.isEmpty()) {
                                    if (permissionString.split(",").length == 5) {
                                        responseObj.setTraining("FULL_ACCESS");
                                    } else if (permissionString.split(",").length == 1){
                                        responseObj.setTraining(permissionString.split("_")[1]);
                                    } else {
                                        responseObj.setTraining(permissionString);
                                    }
                                } else {
                                    responseObj.setTraining("ACCESS_DENIED");
                                }
                                break;
                            case 4:
                                if (!permissionString.isEmpty()) {
                                    if (permissionString.split(",").length == 5) {
                                        responseObj.setUserclass("FULL_ACCESS");
                                    } else if (permissionString.split(",").length == 1){
                                        responseObj.setUserclass(permissionString.split("_")[1]);
                                    } else {
                                        responseObj.setUserclass(permissionString);
                                    }
                                } else {
                                    responseObj.setUserclass("ACESS_DENIED");
                                }
                                break;
                            case 5:
                                if (!permissionString.isEmpty()) {
                                    if (permissionString.split(",").length == 5) {
                                        responseObj.setLearningMaterial("FULL_ACCESS");
                                    } else if (permissionString.split(",").length == 1){
                                        responseObj.setLearningMaterial(permissionString.split("_")[1]);
                                    } else {
                                        responseObj.setLearningMaterial(permissionString);
                                    }
                                } else {
                                    responseObj.setLearningMaterial("ACCESS_DENIED");
                                }
                                break;
                            case 6:
                                if (!permissionString.isEmpty()) {
                                    if (permissionString.split(",").length == 5) {
                                        responseObj.setUserManagement("FULL_ACCESS");
                                    } else if (permissionString.split(",").length == 1){
                                        responseObj.setUserManagement(permissionString.split("_")[1]);
                                    } else {
                                        responseObj.setUserManagement(permissionString);
                                    }
                                } else {
                                    responseObj.setUserManagement("ACCESS_DENIED");
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    countField++;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            responseList.add(responseObj);
        }
        return responseList;
    }


    @Override
    public ResponseObject updatePermission(List<UpdatePermissionRequest> updateRequest) {
        var permissionList = userPermissionDAO.findAll();
        final String[] prefixArr = {"ROLE", "SYLLABUS", "TRAINING", "CLASS", "MATERIAL", "USER"};
        for (UpdatePermissionRequest updatePermission : updateRequest) {
            for (UserPermission permission : permissionList) {
                if (permission.getRole().name().equals(updatePermission.getRoleName())) {
                    int nthChild = 0;
                    Class<?> objClass = updatePermission.getClass();
                    Field[] fields = objClass.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
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
                                switch (nthChild) {
                                    case 1:
                                        permission.setSyllabus(tmpUserPermission);
                                        break;
                                    case 2:
                                        permission.setTrainingProgram(tmpUserPermission);
                                        break;
                                    case 3:
                                        permission.setUserClass(tmpUserPermission);
                                        break;
                                    case 4:
                                        permission.setLearningMaterial(tmpUserPermission);
                                        break;
                                    case 5:
                                        permission.setUserManagement(tmpUserPermission);
                                        break;
                                    default:
                                        break;
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
