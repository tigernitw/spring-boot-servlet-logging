/*
 * Copyright (c) 2023 Shiva Samadhiya <shiva94.nitw@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.tigernitw.logging.config;

import io.github.tigernitw.logging.filter.LoggingFilter;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * {@link LoggingAutoConfig} Logging configuration class to load auto configuration while service
 * comes up.
 */
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

  /** {@link RequestConfig} class to load request configuration. */
  @Data
  public static class RequestConfig {
    private boolean enabled = false;
    private List<String> headers = new ArrayList<String>();
  }

  /** {@link ResponseConfig} class to load response configuration. */
  @Data
  public static class ResponseConfig {
    private boolean enabled = false;
  }

  /**
   * {@link LoggingFilter} class to load logging filter.
   *
   * @param loggingAutoConfig see {@link LoggingAutoConfig}.
   * @return see {@link LoggingFilter}
   */
  @Bean
  public LoggingFilter loggingFilter(LoggingAutoConfig loggingAutoConfig) {
    return new LoggingFilter(loggingAutoConfig);
  }
}
