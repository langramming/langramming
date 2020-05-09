package com.github.langramming.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Optional;

@Data
@ConfigurationProperties("langramming.frontend")
public class LangrammingFrontendConfiguration {
    private Optional<Short> port;
}
