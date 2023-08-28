package test.testspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;



@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TestSpringApplication extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(TestSpringApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TestSpringApplication.class);
	}

}
