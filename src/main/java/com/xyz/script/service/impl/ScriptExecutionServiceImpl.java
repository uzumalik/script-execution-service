package com.xyz.script.service.impl;

import com.xyz.script.config.GroovyConfig;
import com.xyz.script.exception.ExceptionReason;
import com.xyz.script.exception.ServiceException;
import com.xyz.script.payload.request.ScriptExecutionRequest;
import com.xyz.script.payload.response.ScriptExecutionResponse;
import com.xyz.script.service.AsyncScriptExecutionEngine;
import com.xyz.script.service.ScriptExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This service is responsible to have methods for script execution
 *
 * @author Mohammad Uzair
 */
@Service
@Slf4j
public class ScriptExecutionServiceImpl implements ScriptExecutionService {

    private static final String ILLEGAL_SCRIPT_MESSAGE = "General error during canonicalization:";

    @Autowired
    private AsyncScriptExecutionEngine engine;

    @Autowired
    private GroovyConfig groovyConfig;


    @Override
    public ScriptExecutionResponse executeScript(ScriptExecutionRequest request) throws Exception {
        ScriptExecutionResponse response = null;
        CompletableFuture<ScriptExecutionResponse> scriptResult = null;
        try {
            // sanitize scripts here
            // sanitizationService.sanitize(request.getScript());

            // run the script through async engine
            scriptResult = engine.run(request.getScript(), "anyFile", request.getArgs());
            response =  scriptResult.get(groovyConfig.getExecutionTimeOut().toMillis(), TimeUnit.MILLISECONDS);

        } catch (ExecutionException | TimeoutException | MultipleCompilationErrorsException ex) {
            handleExecutionException(request, scriptResult, ex);
        }
        return response;
    }


    private void handleExecutionException(ScriptExecutionRequest request, CompletableFuture<ScriptExecutionResponse> scriptResult, Exception ex) throws Exception {
        Throwable rootException = ExceptionUtils.getRootCause(ex);
        // stop running async thread in case of timeout
        if (rootException instanceof TimeoutException) {
            scriptResult.cancel(true);
            // submit script for auditing
            // auditService.audit(script, args);
        }

        // handle compilation errors
        if (rootException instanceof MultipleCompilationErrorsException) {
            ServiceException exception = null;

            // check if script contains illegal code
            if (null != rootException.getMessage() && rootException.getMessage().contains(ILLEGAL_SCRIPT_MESSAGE)) {
                log.info("Code contains illegal scripts {}", request.getScript());
                // submit script for auditing
                // auditService.audit(script, args);
                exception = new ServiceException("Something went wrong!!", ExceptionReason.UNKNOWN_EXCEPTION);
            } else {

                // this is for genuine compilation errors
                exception = new ServiceException(rootException.getMessage(), ExceptionReason.COMPILATION_ERROR);

            }
            throw exception;
        }


        throw ex;
    }


}
