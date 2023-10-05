package com.example.FAMS.enums;

import com.example.FAMS.services.PermissionService;
import com.example.FAMS.services.UserPermissionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.FAMS.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER(
            Set.of(
            )
    ),
    TRAINER(
            PermissionService.getUserPermission()
    ),
    CLASS_ADMIN(
            Set.of(
                    SYLLABUS_CREATE, SYLLABUS_READ, SYLLABUS_UPDATE, SYLLABUS_DELETE, SYLLABUS_IMPORT,
                    TRAINING_CREATE, TRAINING_READ, TRAINING_UPDATE, TRAINING_DELETE, TRAINING_IMPORT,
                    CLASS_CREATE, CLASS_READ, CLASS_UPDATE, CLASS_DELETE, CLASS_IMPORT
            )
    ),
    SUPER_ADMIN (
            Set.of(
                    SYLLABUS_CREATE, SYLLABUS_READ, SYLLABUS_UPDATE, SYLLABUS_DELETE, SYLLABUS_IMPORT,
                    TRAINING_CREATE, TRAINING_READ, TRAINING_UPDATE, TRAINING_DELETE, TRAINING_IMPORT,
                    CLASS_CREATE, CLASS_READ, CLASS_UPDATE, CLASS_DELETE, CLASS_IMPORT,
                    USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE, USER_IMPORT
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authList =getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authList.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authList;
    }

}
