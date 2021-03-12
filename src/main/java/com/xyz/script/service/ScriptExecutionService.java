package com.xyz.script.service;


import com.xyz.script.payload.request.ScriptExecutionRequest;
import com.xyz.script.payload.response.ScriptExecutionResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * this interface is responsible tp have method to execute scripts
 * @author Mohammad Uzair
 */
public interface ScriptExecutionService {

    ScriptExecutionResponse executeScript(ScriptExecutionRequest request) throws Exception;
}
