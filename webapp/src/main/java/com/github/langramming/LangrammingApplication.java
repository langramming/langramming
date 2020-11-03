package com.github.langramming;

import com.github.langramming.configuration.LangrammingFrontendConfiguration;
import com.github.langramming.configuration.LangrammingServerConfiguration;
import com.github.langramming.configuration.LangrammingSpotifyConfiguration;
import com.github.langramming.configuration.LangrammingTelegramConfiguration;
import javax.inject.Singleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(
    {
        LangrammingServerConfiguration.class,
        LangrammingFrontendConfiguration.class,
        LangrammingTelegramConfiguration.class,
        LangrammingSpotifyConfiguration.class,
    }
)
@ComponentScan(
    basePackages = "com.github.langramming",
    includeFilters = @ComponentScan.Filter({ Singleton.class })
)
public class LangrammingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LangrammingApplication.class, args);
    }
}
