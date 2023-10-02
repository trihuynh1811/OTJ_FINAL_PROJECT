package com.example.FAMS.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/syllabus")
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
public class SyllabusController {

    @GetMapping
    @PreAuthorize("hasAuthority('syllabus:read')")
    public String get() {
        return "GET:: syllabus controller";
    }

}
