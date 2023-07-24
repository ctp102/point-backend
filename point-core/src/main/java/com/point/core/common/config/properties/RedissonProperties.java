package com.point.core.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("redisson.lock")
public class RedissonProperties {

    private Long waitTime;
    private Long leaseTime;

}
