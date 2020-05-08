package com.github.langramming.httpserver;

import com.github.langramming.util.EnvironmentVariables;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FrontendConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (EnvironmentVariables.FRONTEND_PORT.isEmpty()) {
            registry.addResourceHandler("/**").addResourceLocations("classpath:/assets/");
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
