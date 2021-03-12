package com.xyz.script.service.provider;

import com.xyz.script.ScriptExecutionServiceConfig;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Thsi class provides the custom groovy shell to run the scripts in sandbox model
 * @author Mohammad Uzair
 *
 */
@Component
public class GroovyShellProvider {


    @Autowired
    private ScriptExecutionServiceConfig serviceConfig;

    /**
     * provides a secure shell with thread safe bindings
     * @param binding
     * @return
     */
    public GroovyShell getShell(Binding binding){

        final CompilerConfiguration config = new CompilerConfiguration();
        config.addCompilationCustomizers(serviceConfig.getCompilationConfig());
        return new GroovyShell(binding, config);

    }
}
