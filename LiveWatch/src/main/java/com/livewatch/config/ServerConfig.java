package com.livewatch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="app.server")
public class ServerConfig {

    private long maxUsers;

    private long warningThreshold;

    private boolean enableLoadBalancing;
}
