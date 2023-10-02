package com.example.FAMS;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repository.UserPermissionRepo;
import com.example.FAMS.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FamsApplicationTests {

	@SpyBean
	UserPermissionRepo userPermissionRepo;

	@SpyBean
	UserRepo userRepo;

	@SpyBean
	PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
	}

	@Test
	void insertUser(){
		List<User> userList = new ArrayList<>();
		User user1 = User.builder()
				.name("admin")
				.phone("9112001")
				.email("admin@gmail.com")
				.classUsers(null)
				.role(Role.CLASSS_ADMIN)
				.userPermission(userPermissionRepo.findById(1).get())
				.password(passwordEncoder.encode("123"))
				.build();

		User user2 = User.builder()
				.name("super_admin")
				.phone("91120012")
				.email("superadmin@gmail.com")
				.classUsers(null)
				.role(Role.SUPER_ADMIN)
				.userPermission(userPermissionRepo.findById(2).get())
				.password(passwordEncoder.encode("123"))
				.build();

		userList.add(user1);
		userList.add(user2);
		userRepo.saveAll(userList);
	}

	@Test
	void insertUserPermission(){
		List<UserPermission> userPermissionList = new ArrayList<>();
		UserPermission userPermission1 = UserPermission.builder()
				.permissionId(1)
				.userClass("wtf is user class again")
				.role(Role.CLASSS_ADMIN)
				.learningMaterial("how to 9/11")
				.syllabus("wtf is syllabus")
				.users(null)
				.build();

		UserPermission userPermission2 = UserPermission.builder()
				.permissionId(2)
				.userClass("wtf is user class again")
				.role(Role.SUPER_ADMIN)
				.learningMaterial("how to 9/11 part 2")
				.syllabus("wtf is syllabus")
				.users(null)
				.build();

		userPermissionList.add(userPermission1);
		userPermissionList.add(userPermission2);
		userPermissionRepo.saveAll(userPermissionList);
	}

}
