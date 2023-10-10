package com.example.FAMS;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class FamsApplication {

	private final UserDAO userDAO;
	@Autowired
	private UserPermissionDAO userPermissionDAO;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(FamsApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner initData() {
//		return new CommandLineRunner() {
//			@Override
//			public void run(String... args) throws Exception {
//				List<UserPermission> userPermissionList = new ArrayList<>();
//
//				userPermissionList.add(
//						UserPermission.builder()
//								.learningMaterial("some bs").syllabus("some more bs").userClass("the fuck is user class").role(Role.SUPER_ADMIN)
//								.build()
//				);
//				userPermissionList.add(
//						UserPermission.builder()
//								.learningMaterial("some bs").syllabus("some more bs").userClass("the fuck is user class").role(Role.CLASS_ADMIN)
//								.build()
//				);
//				userPermissionList.add(
//						UserPermission.builder()
//								.learningMaterial("some bs").syllabus("some more bs").userClass("the fuck is user class").role(Role.TRAINER)
//								.build()
//				);
//				userPermissionList.add(
//						UserPermission.builder()
//								.learningMaterial("some bs").syllabus("some more bs").userClass("the fuck is user class").role(Role.USER)
//								.build()
//				);
//				userPermissionDAO.saveAll(userPermissionList);
//
//
//
//				List<User> userList = new ArrayList<>();
//				User admin = User.builder()
//						.email("admin@gmail.com")
//						.password(passwordEncoder.encode("1"))
//						.name("Admin")
//						.phone("0977545450")
//						.dob(new Date())
//						.gender("Male")
//						.userPermission(userPermissionDAO.findById(1).get())
//						.status("i want to kill myself")
//						.createdBy("Hoang Anh")
//						.createdDate(new Date())
//						.modifiedBy("Hoang Anh")
//						.modifiedDate(new Date())
//						.build();
//
//				User superAdmin = User.builder()
//						.email("superadmin@gmail.com")
//						.password(passwordEncoder.encode("1"))
//						.name("Super_Admin")
//						.phone("0977545452")
//						.dob(new Date())
//						.gender("Microwave")
//						.userPermission(userPermissionDAO.findById(2).get())
//						.status("i still want to kill myself")
//						.createdBy("Hoang Anh")
//						.createdDate(new Date())
//						.modifiedBy("Hoang Anh")
//						.modifiedDate(new Date())
//						.build();
//
//				User trainer = User.builder()
//						.email("trainer@gmail.com")
//						.password(passwordEncoder.encode("1"))
//						.name("Trainer")
//						.phone("0977545453")
//						.dob(new Date())
//						.gender("Microwave")
//						.userPermission(userPermissionDAO.findById(3).get())
//						.status("ಠ_ಠ")
//						.createdBy("Hoang Anh")
//						.createdDate(new Date())
//						.modifiedBy("Hoang Anh")
//						.modifiedDate(new Date())
//						.build();
//
//				User joe = User.builder()
//						.email("user@gmail.com")
//						.password(passwordEncoder.encode("1"))
//						.name("Joe")
//						.phone("0977545454")
//						.dob(new Date())
//						.gender("Microwave")
//						.userPermission(userPermissionDAO.findById(4).get())
//						.status("💪(⌐■_■)")
//						.createdBy("Hoang Anh")
//						.createdDate(new Date())
//						.modifiedBy("Hoang Anh")
//						.modifiedDate(new Date())
//						.build();
//
//				userList.add(admin);
//				userList.add(superAdmin);
//				userList.add(trainer);
//				userList.add(joe);
//				userDAO.saveAll(userList);
//			}
//		};
//	}

}
