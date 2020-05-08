package com.github.langramming;

import com.github.langramming.client.telegram.TelegramStarter;
import com.github.langramming.database.LangrammingDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.inject.Singleton;

@SpringBootApplication
@ComponentScan(
		basePackages = "com.github.langramming",
		includeFilters = @ComponentScan.Filter({ Singleton.class }))
public class LangrammingApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LangrammingApplication.class, args);

		context.getBean(LangrammingDatabase.class).start();
		context.getBean(TelegramStarter.class).start();
	}

}
