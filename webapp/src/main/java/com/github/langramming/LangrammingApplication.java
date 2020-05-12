package com.github.langramming;

import com.github.langramming.client.telegram.TelegramStarter;
import com.github.langramming.configuration.LangrammingFrontendConfiguration;
import com.github.langramming.configuration.LangrammingServerConfiguration;
import com.github.langramming.configuration.LangrammingSpotifyConfiguration;
import com.github.langramming.configuration.LangrammingTelegramConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.inject.Singleton;

@SpringBootApplication
@EnableConfigurationProperties({
		LangrammingServerConfiguration.class,
		LangrammingFrontendConfiguration.class,
		LangrammingTelegramConfiguration.class,
		LangrammingSpotifyConfiguration.class })
@ComponentScan(
		basePackages = "com.github.langramming",
		includeFilters = @ComponentScan.Filter({ Singleton.class }))
public class LangrammingApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LangrammingApplication.class, args);

		context.getBean(TelegramStarter.class).start();
	}

}