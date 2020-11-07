package dev.nickrobson.langramming;

import dev.nickrobson.langramming.configuration.LangrammingFrontendConfiguration;
import dev.nickrobson.langramming.configuration.LangrammingServerConfiguration;
import dev.nickrobson.langramming.configuration.LangrammingSpotifyConfiguration;
import dev.nickrobson.langramming.configuration.LangrammingTelegramConfiguration;
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
