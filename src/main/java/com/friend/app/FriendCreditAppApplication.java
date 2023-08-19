package com.friend.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableJpaAuditing
public class FriendCreditAppApplication {

	public static void main(String[] args) {

		ApplicationContext ctx = SpringApplication.run(FriendCreditAppApplication.class, args);
	}
}
