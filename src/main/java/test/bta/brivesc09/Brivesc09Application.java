package test.bta.brivesc09;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import test.bta.brivesc09.repository.JenjangDb;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.restcontroller.UserRestController;

import java.text.ParseException;

@SpringBootApplication
public class Brivesc09Application extends SpringBootServletInitializer {

	public static void main(String[] args) throws ParseException {
		SpringApplication.run(Brivesc09Application.class, args);
	}

	static void addAdmin() throws ParseException {
		// UserRestController userRestController = new UserRestController();
		// StaffDTO staffDTO = new StaffDTO();
		// staffDTO.email = "default";
		// staffDTO.namaLengkap = "admin default";
		// staffDTO.noHP = "08080808";
		// staffDTO.noPegawai = "08080808";
		// staffDTO.username = "admin";
		// staffDTO.password = "admin";
		// staffDTO.role = "ADMIN";
		// userRestController.createUserStaff(staffDTO);
	}

	static void addJenjang() {

	}

	static void addRole() {

	}

	@Bean
  	public BCryptPasswordEncoder bCryptPasswordEncoder() {
    	return new BCryptPasswordEncoder();
	}

//	@Bean
//	public ServletWebServerFactory servletWebServerFactory() {
//		return new TomcatServletWebServerFactory();
//	}

}
