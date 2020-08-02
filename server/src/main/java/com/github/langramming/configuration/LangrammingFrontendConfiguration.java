package com.github.langramming.configuration;

import java.util.Optional;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("langramming.frontend")
public class LangrammingFrontendConfiguration {
    private Optional<Short> port;
}
