package com.github.tigernitw.logging.config;

import com.github.tigernitw.logging.filter.LoggingFilter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "servlet-logging")
public class LoggingAutoConfig {

    private String applicationName = "application";

    private List<String> ignorePatterns = new ArrayList<String>();

    private RequestConfig requestConfig;

    private ResponseConfig responseConfig;

    @Data
    public static class RequestConfig {
        private boolean enabled = false;
        private List<String> headers = new ArrayList<String>();
    }

    @Data
    public static class ResponseConfig {
        private boolean enabled = false;
    }

    @Bean
    public LoggingFilter loggingFilter(LoggingAutoConfig loggingAutoConfig) {
        return new LoggingFilter(loggingAutoConfig);
    }

}
