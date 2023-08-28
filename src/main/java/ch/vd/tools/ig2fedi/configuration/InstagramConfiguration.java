package ch.vd.tools.ig2fedi.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InstagramProperties.class)
public class InstagramConfiguration {
}
