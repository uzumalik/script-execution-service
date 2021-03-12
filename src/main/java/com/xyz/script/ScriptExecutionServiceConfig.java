package com.xyz.script;

import com.xyz.script.config.GroovyConfig;
import com.xyz.script.service.provider.CompilationProvider;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Mohammad Uzair
 */
@Configuration
public class ScriptExecutionServiceConfig {

    @Autowired
    private GroovyConfig groovyConfig;

    @Bean
    public CompilationCustomizer getCompilationConfig(){

        return new CompilationProvider().createCompilationCustomizer(groovyConfig);

    }
}
