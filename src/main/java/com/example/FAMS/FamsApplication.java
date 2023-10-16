package com.example.FAMS;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.StandardOutput;
import com.example.FAMS.models.User;
import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repositories.*;
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

	@Autowired
	private SyllabusDAO syllabusDAO;

	@Autowired
	private StandardOutputDAO standardOutputDAO;

	@Autowired
	private TrainingProgramDAO trainingProgramDAO;

	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(FamsApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData() {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
//				List<StandardOutput> standardOutputList = new ArrayList<>();
//
//				String[] objectiveCode = {"h4sd", "h6sd", "k6sd", "hk416", "mp5k", "mac10", "m4a1"};
//
//				for (int i = 0; i < objectiveCode.length; i++){
//					StandardOutput standardOutput = StandardOutput.builder()
//							.outputCode(objectiveCode[i].toUpperCase())
//							.outputName(objectiveCode[i])
//							.description("some bs description.")
//							.build();
//
//					standardOutputList.add(standardOutput);
//				}
//
//				standardOutputDAO.saveAll(standardOutputList);
//
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

//				List<Syllabus> syllabusList = new ArrayList<>();
//
//				Syllabus syllabus1 = Syllabus.builder()
//						.version("1.0")
//						.topicCode("9/11")
//						.trainingAudience(10)
//						.topicName("world trade center")
//						.createdBy("osama bin-laden")
//						.createdDate(new Date("2001/09/11"))
//						.priority("high")
//						.technicalGroup("OS: Windows XP/Vista/7/8 or 10. Processor: 2.3 GHz Intel Core 2 Duo or better. Memory: 512 MB RAM.")
//						.modifiedBy("")
//						.modifiedDate(null)
//						.userID(userDAO.findById(2).get())
//						.topicOutline("wtf is topic outline")
//						.publishStatus("draft")
//						.trainingMaterials("how to 9/11 part 2 book")
//						.trainingPrinciples("hit the world trade center tower")
//						.build();
//
//				Syllabus syllabus2 = Syllabus.builder()
//						.version("1.1")
//						.topicCode("4/06")
//						.trainingAudience(1000)
//						.topicName("tian ming square")
//						.createdBy("beijing student")
//						.createdDate(new Date("1989/06/04"))
//						.priority("high")
//						.technicalGroup("OS: Windows XP/Vista/7/8 or 10. Processor: 2.3 GHz Intel Core 2 Duo or better. Memory: 512 MB RAM.")
//						.modifiedBy("")
//						.modifiedDate(null)
//						.userID(userDAO.findById(2).get())
//						.topicOutline("wtf is topic outline")
//						.publishStatus("active")
//						.trainingMaterials("tian ming square massacre")
//						.trainingPrinciples("protest")
//						.build();
//
//				Syllabus syllabus3 = Syllabus.builder()
//						.version("9.9")
//						.topicCode("9/11(2)")
//						.trainingAudience(10)
//						.topicName("world trade center")
//						.createdBy("osama bin-laden")
//						.createdDate(new Date("1999/11/09"))
//						.priority("high")
//						.technicalGroup("OS: Windows XP/Vista/7/8 or 10. Processor: 2.3 GHz Intel Core 2 Duo or better. Memory: 512 MB RAM.")
//						.modifiedBy("")
//						.modifiedDate(null)
//						.userID(userDAO.findById(1).get())
//						.topicOutline("wtf is topic outline")
//						.publishStatus("inactive")
//						.trainingMaterials("how to 9/11 part 2 book")
//						.trainingPrinciples("hit the world trade center tower")
//						.build();
//
//				syllabusList.add(syllabus1);
//				syllabusList.add(syllabus2);
//				syllabusList.add(syllabus3);
//
//				syllabusDAO.saveAll(syllabusList);


//				TrainingProgram trainingProgram = TrainingProgram.builder()
//						.createdBy("joe biden")
//						.createdDate(new Date())
//						.duration(120)
//						.trainingProgramCode(1)
//						.modifiedBy("")
//						.modifiedDate(new Date())
//						.userID(userDAO.findById(1).get())
//						.name("lmao fuck this shit")
//						.startDate(new Date("2023/11/18"))
//						.status("active")
//						.topicCode("4/06")
//						.build();
//
//				trainingProgramDAO.save(trainingProgram);
			}
		};
	}

}
