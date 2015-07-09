package dwalldorf.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application config
 */
@Configuration
@ComponentScan("dwalldorf")
@EnableScheduling
@PropertySources({
  @PropertySource(value = "classpath:application.properties")
})
public class AppConfig {
}