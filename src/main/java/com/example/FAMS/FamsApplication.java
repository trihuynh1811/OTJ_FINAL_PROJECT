package com.example.FAMS;

import com.example.FAMS.enums.Role;
import com.example.FAMS.models.User;
import com.example.FAMS.repositories.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
@RequiredArgsConstructor
public class FamsApplication {

	private final UserDAO userDAO;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(FamsApplication.class, args);
	}


    @Bean
    public CommandLineRunner initData() {
        return new CommandLineRunner() {
			@Override
            public void run(String... args) throws Exception {
                User admin = User.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("1"))
                        .name("Admin")
                        .phone("0977545450")
                        .dob(new Date())
                        .gender("Male")
                        .role(Role.SUPER_ADMIN)
                        .status("Wonderful")
                        .createdBy("Hoang Anh")
                        .createdDate(new Date())
                        .modifiedBy("Hoang Anh")
                        .modifiedDate(new Date())
                        .build();
                userDAO.save(admin);
            }
        };
    }

}
