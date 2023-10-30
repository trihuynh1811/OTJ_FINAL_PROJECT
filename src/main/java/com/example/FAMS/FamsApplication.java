package com.example.FAMS;

import com.example.FAMS.dto.UserDTO;
import com.example.FAMS.dto.requests.LoginRequest;
import com.example.FAMS.enums.Role;
import com.example.FAMS.models.StandardOutput;
import com.example.FAMS.models.Syllabus;
import com.example.FAMS.models.User;
import com.example.FAMS.models.UserPermission;
import com.example.FAMS.repositories.StandardOutputDAO;
import com.example.FAMS.repositories.SyllabusDAO;
import com.example.FAMS.repositories.UserDAO;
import com.example.FAMS.repositories.UserPermissionDAO;
import com.example.FAMS.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.FAMS.enums.Permission.*;

@SpringBootApplication
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/")
public class FamsApplication {

  private final UserDAO userDAO;
  private final UserPermissionDAO userPermissionDAO;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationService authenticationService;
  private final StandardOutputDAO standardOutputDAO;
  private final SyllabusDAO syllabusDAO;
  private Logger logger = LoggerFactory.getLogger(FamsApplication.class);
  public static void main(String[] args) {
    SpringApplication.run(FamsApplication.class, args);
  }

  @Bean
  public CommandLineRunner initData() {
    return new CommandLineRunner() {
      @Override
      public void run(String... args) throws Exception {
        if (standardOutputDAO.findAll().size() == 0) {
          List<StandardOutput> standardOutputList = new ArrayList<>();
          String[] objectiveCode = {"h4sd", "h6sd", "k6sd", "hk416", "mp5k", "mac10", "m4a1"};
          for (int i = 0; i < objectiveCode.length; i++) {
            StandardOutput standardOutput =
                StandardOutput.builder()
                    .outputCode(objectiveCode[i].toUpperCase())
                    .outputName(objectiveCode[i])
                    .description("some bs description.")
                    .build();

            standardOutputList.add(standardOutput);
          }
          standardOutputDAO.saveAll(standardOutputList);
        }
        if (userPermissionDAO.findAll().size() == 0) {
          List<UserPermission> permissionList = new ArrayList<>();
          permissionList.add(
              UserPermission.builder()
                  .role(Role.SUPER_ADMIN)
                  .syllabus(
                      List.of(
                          SYLLABUS_CREATE,
                          SYLLABUS_VIEW,
                          SYLLABUS_MODIFY,
                          SYLLABUS_DELETE,
                          SYLLABUS_IMPORT))
                  .trainingProgram(
                      List.of(
                          TRAINING_CREATE,
                          TRAINING_VIEW,
                          TRAINING_MODIFY,
                          TRAINING_DELETE,
                          TRAINING_IMPORT))
                  .userClass(
                      List.of(CLASS_CREATE, CLASS_VIEW, CLASS_MODIFY, CLASS_DELETE, CLASS_IMPORT))
                  .userManagement(
                      List.of(USER_CREATE, USER_VIEW, USER_MODIFY, USER_DELETE, USER_IMPORT))
                  .learningMaterial(List.of())
                  .build());
          permissionList.add(
              UserPermission.builder()
                  .role(Role.TRAINER)
                  .syllabus(
                      List.of(
                          SYLLABUS_CREATE,
                          SYLLABUS_VIEW,
                          SYLLABUS_MODIFY,
                          SYLLABUS_DELETE,
                          SYLLABUS_IMPORT))
                  .trainingProgram(List.of(TRAINING_VIEW))
                  .userClass(List.of(CLASS_VIEW))
                  .userManagement(List.of())
                  .learningMaterial(List.of())
                  .build());
          permissionList.add(
              UserPermission.builder()
                  .role(Role.USER)
                  .syllabus(List.of())
                  .trainingProgram(List.of())
                  .userClass(List.of())
                  .userManagement(List.of())
                  .learningMaterial(List.of())
                  .build());
          permissionList.add(
              UserPermission.builder()
                  .role(Role.CLASS_ADMIN)
                  .syllabus(
                      List.of(
                          SYLLABUS_CREATE,
                          SYLLABUS_VIEW,
                          SYLLABUS_MODIFY,
                          SYLLABUS_DELETE,
                          SYLLABUS_IMPORT))
                  .trainingProgram(
                      List.of(
                          TRAINING_CREATE,
                          TRAINING_VIEW,
                          TRAINING_MODIFY,
                          TRAINING_DELETE,
                          TRAINING_IMPORT))
                  .userClass(
                      List.of(CLASS_CREATE, CLASS_VIEW, CLASS_MODIFY, CLASS_DELETE, CLASS_IMPORT))
                  .userManagement(
                      List.of(USER_CREATE, USER_VIEW, USER_MODIFY, USER_DELETE, USER_IMPORT))
                  .learningMaterial(List.of())
                  .build());
          userPermissionDAO.saveAll(permissionList);
        }
        if (userDAO.findAll().size() == 0) {
          List<User> userList = new ArrayList<>();
          userList.add(
              User.builder()
                  .email("admin@gmail.com")
                  .password(passwordEncoder.encode("1"))
                  .name("Admin")
                  .phone("0977545450")
                  .dob(new Date())
                  .gender("male")
                  .role(userPermissionDAO.findUserPermissionByRole(Role.SUPER_ADMIN).orElse(null))
                  .status(true)
                  .createdBy("Hoang Anh")
                  .createdDate(new Date())
                  .modifiedBy("Hoang Anh")
                  .modifiedDate(new Date())
                  .build());
          userList.add(
              User.builder()
                  .email("classadmin@gmail.com")
                  .password(passwordEncoder.encode("1"))
                  .name("Class Admin")
                  .phone("0977545451")
                  .dob(new Date())
                  .gender("male")
                  .role(userPermissionDAO.findUserPermissionByRole(Role.CLASS_ADMIN).orElse(null))
                  .status(true)
                  .createdBy("Hoang Anh")
                  .createdDate(new Date())
                  .modifiedBy("Hoang Anh")
                  .modifiedDate(new Date())
                  .build());
          userList.add(
              User.builder()
                  .email("trainer@gmail.com")
                  .password(passwordEncoder.encode("1"))
                  .name("Trainer")
                  .phone("0977545452")
                  .dob(new Date())
                  .gender("male")
                  .role(userPermissionDAO.findUserPermissionByRole(Role.TRAINER).orElse(null))
                  .status(true)
                  .createdBy("Hoang Anh")
                  .createdDate(new Date())
                  .modifiedBy("Hoang Anh")
                  .modifiedDate(new Date())
                  .build());
          userList.add(
              User.builder()
                  .email("user@gmail.com")
                  .password(passwordEncoder.encode("1"))
                  .name("User")
                  .phone("0977545453")
                  .dob(new Date())
                  .gender("male")
                  .role(userPermissionDAO.findUserPermissionByRole(Role.USER).orElse(null))
                  .status(true)
                  .createdBy("Hoang Anh")
                  .createdDate(new Date())
                  .modifiedBy("Hoang Anh")
                  .modifiedDate(new Date())
                  .build());
          userDAO.saveAll(userList);

          logger.info(
              "SUPER_ADMIN Token: "
                  + authenticationService
                      .login(
                          LoginRequest.builder()
                              .email(userList.get(0).getEmail())
                              .password("1")
                              .build())
                      .getToken());
          logger.info(
              "CLASS_ADMIN Token: "
                  + authenticationService
                      .login(
                          LoginRequest.builder()
                              .email(userList.get(1).getEmail())
                              .password("1")
                              .build())
                      .getToken());
          logger.info(
              "TRAINER Token: "
                  + authenticationService
                      .login(
                          LoginRequest.builder()
                              .email(userList.get(2).getEmail())
                              .password("1")
                              .build())
                      .getToken());
          logger.info(
              "USER Token: "
                  + authenticationService
                      .login(
                          LoginRequest.builder()
                              .email(userList.get(3).getEmail())
                              .password("1")
                              .build())
                      .getToken());
        }
        if (syllabusDAO.findAll().size() == 0) {
          List<Syllabus> syllabusList = new ArrayList<>();
          syllabusList.add(
              Syllabus.builder()
                  .topicCode("TOPIC001")
                  .topicName("OOP")
                  .technicalGroup("Team1")
                  .version("1.0")
                  .trainingAudience(1)
                  .topicOutline("Topic Outline 1")
                  .trainingMaterials("Training Materials 1")
                  .trainingPrinciples("Training Principles 1")
                  .priority("High")
                  .publishStatus("Published")
                  .createdBy("User1")
                  .createdDate(new Date())
                  .modifiedBy("User1")
                  .modifiedDate(new Date())
                  .userID(userDAO.findById(1).orElse(null))
                  .courseObjective("IDK")
                  .build());
          syllabusList.add(
              Syllabus.builder()
                  .topicCode("TOPIC002")
                  .topicName("LAB")
                  .technicalGroup("Team2")
                  .version("1.0")
                  .trainingAudience(1)
                  .topicOutline("Topic Outline 2")
                  .trainingMaterials("Training Materials 2")
                  .trainingPrinciples("Training Principles 2")
                  .priority("High")
                  .publishStatus("Published")
                  .createdBy("User1")
                  .createdDate(new Date())
                  .modifiedBy("User1")
                  .modifiedDate(new Date())
                  .userID(userDAO.findById(2).orElse(null))
                  .courseObjective("IDK")
                  .build());
          syllabusList.add(
              Syllabus.builder()
                  .topicCode("TOPIC003")
                  .topicName("OOP")
                  .technicalGroup("Team3")
                  .version("1.0")
                  .trainingAudience(1)
                  .topicOutline("Topic Outline 3")
                  .trainingMaterials("Training Materials 3")
                  .trainingPrinciples("Training Principles 3")
                  .priority("High")
                  .publishStatus("Published")
                  .createdBy("User3")
                  .createdDate(new Date())
                  .modifiedBy("User3")
                  .modifiedDate(new Date())
                  .userID(userDAO.findById(3).orElse(null))
                  .courseObjective("IDK")
                  .build());
          syllabusDAO.saveAll(syllabusList);
        }
      }
    };
  }

  @GetMapping("")
  public String greeting() {
    return "Hello from FAMS Application made from GROUP 1 WITH LOVE";
  }
}
