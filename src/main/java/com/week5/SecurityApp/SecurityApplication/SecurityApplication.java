package com.week5.SecurityApp.SecurityApplication;

//import com.week5.SecurityApp.SecurityApplication.services.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class SecurityApplication implements CommandLineRunner {

//	private final DataService dataService;

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		System.out.println("Running");
	}
}
