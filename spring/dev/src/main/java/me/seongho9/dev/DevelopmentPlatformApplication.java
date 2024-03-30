package me.seongho9.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class DevelopmentPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevelopmentPlatformApplication.class, args);
	}

}
