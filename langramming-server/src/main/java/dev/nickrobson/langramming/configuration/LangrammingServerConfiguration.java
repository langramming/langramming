package dev.nickrobson.langramming.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("langramming.server")
public class LangrammingServerConfiguration {
    private String url;
}
