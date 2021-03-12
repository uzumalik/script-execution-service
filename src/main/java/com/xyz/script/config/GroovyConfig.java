package com.xyz.script.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohammad Uzair
 */

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "groovy-config")
@Getter
@Setter
@Validated
public class GroovyConfig {

    @NotNull
    private SecurityConf security;

    @NotNull
    private Duration executionTimeOut;

    @lombok.Getter
    @lombok.Setter
    public static class SecurityConf {
        @NotEmpty
        private List<String> importsBlacklist;

        @NotEmpty
        private List<String> starImportsBlacklist;

        @NotEmpty
        private Set<String> illegalClasses;

        @NotEmpty
        private Set<String> illegalAssignmentClasses;

        @NotEmpty
        private Map<String, Set<String>> illegalProperties;

        @NotEmpty
        private Map<String, Set<String>> illegalMethods;
    }


}

