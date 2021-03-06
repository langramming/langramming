package dev.nickrobson.langramming.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("langramming.spotify")
public class LangrammingSpotifyConfiguration {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
