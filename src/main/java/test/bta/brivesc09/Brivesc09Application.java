package test.bta.brivesc09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Brivesc09Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
//		new SpringApplicationBuilder(Brivesc09Application.class)
//				.web(WebApplicationType.SERVLET)
//				.run(args);
		SpringApplication.run(Brivesc09Application.class, args);


	}

	static void addAdmin() {

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
