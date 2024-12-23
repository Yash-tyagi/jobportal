package com.job4us.jobportal;

import com.job4us.jobportal.entity.User;
import com.job4us.jobportal.entity.UserRole;
import com.job4us.jobportal.service.UserRoleService;
import com.job4us.jobportal.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class JobportalApplication {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(JobportalApplication.class, args);
	}

}
