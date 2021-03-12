package com.xyz.script.service.impl;

import com.xyz.script.exception.ExceptionReason;
import com.xyz.script.exception.ServiceException;
import com.xyz.script.payload.response.ScriptExecutionResponse;
import com.xyz.script.service.AsyncScriptExecutionEngine;
import com.xyz.script.service.provider.GroovyShellProvider;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.IFileNameFinder;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;

/**
 * @author Mohammad Uzair
 */
@Service
@Async
@Slf4j
public class AsyncScriptExecutionEngineImpl implements AsyncScriptExecutionEngine {

    private static final String SYSTEM_OUT = "out";

    @Autowired
    private GroovyShellProvider shellProvider;

    @Override
    public CompletableFuture<ScriptExecutionResponse> run(String script, String fileName, String... args) throws IOException {

        try (
                // Using PrintWriter to capture contents printed by printLn
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
        ) {
            // binding to get console prints
            Binding binding = new Binding();
            binding.setProperty(SYSTEM_OUT, printWriter);
            GroovyShell shell = shellProvider.getShell(binding);

            Object result = shell.run(script, fileName, args);

            ScriptExecutionResponse response = ScriptExecutionResponse
                    .builder()
                    .executionResult(null == result ? null : result.toString())
                    .prints(stringWriter.toString())
                    .build();
            return CompletableFuture.completedFuture(response);
        }
    }
}
