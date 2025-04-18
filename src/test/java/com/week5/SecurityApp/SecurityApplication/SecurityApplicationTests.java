package com.week5.SecurityApp.SecurityApplication;

import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.services.JwtService;
import com.week5.SecurityApp.SecurityApplication.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityApplicationTests {

	@Autowired
	private JwtService jwtService;



	@Test
	void contextLoads() {

//		UserEntity userEntity = new UserEntity(4L,"mritunjay@gmail.com","1223");
//
//		String token = jwtService.generateToken(userEntity);
//
//		System.out.println(token);
//
//		Long id = jwtService.getUserIdFromToken(token);
//
//		System.out.println(id);

	}

}
