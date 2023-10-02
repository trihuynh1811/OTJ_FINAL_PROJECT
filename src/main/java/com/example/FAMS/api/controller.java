package com.example.FAMS.api;

import com.example.FAMS.enums.Permission;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.models.UserPermission;
import com.example.FAMS.service.AuthenticationRequest;
import com.example.FAMS.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class controller {

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @RequestMapping(value = "/super/admin", method = RequestMethod.GET)
    public ResponseEntity<String> getSuperAdmin(){
        return ResponseEntity.status(200).body("super admin page");
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.status(200).body("admin page");
    }

    @RequestMapping(value = "/trainer", method = RequestMethod.GET)
    public ResponseEntity<String> getTrainer() {
        return ResponseEntity.status(200).body("trainer page");
    }

}
