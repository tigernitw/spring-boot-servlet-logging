# spring-boot-servlet-logging
Spring boot library to customise servlet logs based on path
- Enable/Disable servlet request & response logs based on configuration.
- All logs including servlet request & response will have unique trace-id based on project logging appender pattern.
- URL path regex configuration to disable logs for particular pattern of URLs.

###### Requirements
- Java 17 & above 
- Maven > 3.0
- Spring boot framework > 3.0.0

###### Usage
- Add below dependency
    ```xml
   <dependency>
        <groupId>io.github.tigernitw</groupId>
        <artifactId>servlet-logging-spring-boot-starter</artifactId>
        <version>1.0</version>
    </dependency>
    ```
- Add below config 
    ```yaml
   servlet-logging:
      request-config:
        enabled: true
      response-config:
        enabled: true
    ```
