package sampleAPIs.restserver.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Getter
@Setter
@Slf4j
public class ApplicationConfig {

  @Value("${basePathForYamls}")
  private String basePathForYamls;

}