package com.example.FAMS.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.FAMS.enums.Permission.*;

@RequiredArgsConstructor
public enum Role {
    USER,
    TRAINER,
    CLASS_ADMIN,
    SUPER_ADMIN
}
