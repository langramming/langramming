package dev.nickrobson.langramming.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("langramming.telegram")
public class LangrammingTelegramConfiguration {
    private String token;
}
