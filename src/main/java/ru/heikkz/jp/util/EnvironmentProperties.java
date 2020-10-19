package ru.heikkz.jp.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jp")
@Getter
@Setter
@Component
public class EnvironmentProperties {

    private String accountVerificationUrl;
}
