package com.graduation.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemProperties {
    private Integer tokenExpireTime;
}
