package com.bastiansmn.vp;

import com.bastiansmn.vp.authorities.AuthoritiesService;
import com.bastiansmn.vp.authorities.dto.AuthoritiesCreationDTO;
import com.bastiansmn.vp.role.RoleService;
import com.bastiansmn.vp.role.dto.RoleCreationDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class VpApplication {

	public static void main(String[] args) {
		SpringApplication.run(VpApplication.class, args);
	}


	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	CommandLineRunner run(AuthoritiesService authoritiesService, RoleService roleService) {
//		return args -> {
//			try {
//				authoritiesService.create(new AuthoritiesCreationDTO("create"));
//				authoritiesService.create(new AuthoritiesCreationDTO("read"));
//				authoritiesService.create(new AuthoritiesCreationDTO("update"));
//				authoritiesService.create(new AuthoritiesCreationDTO("delete"));
//
//				roleService.create(new RoleCreationDTO("ROLE_ADMIN"));
//				roleService.create(new RoleCreationDTO("ROLE_USER"));
//			} catch (DataIntegrityViolationException ignore) {}
//		};
//	}


}
