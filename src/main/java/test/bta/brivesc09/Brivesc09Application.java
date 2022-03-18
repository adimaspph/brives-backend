package test.bta.brivesc09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Brivesc09Application {

	public static void main(String[] args) {
		SpringApplication.run(Brivesc09Application.class, args);
	}

	@Bean
  	public BCryptPasswordEncoder bCryptPasswordEncoder() {
    	return new BCryptPasswordEncoder();
	}
    

}
