package com.xyz.script.service;

import com.xyz.script.payload.response.ScriptExecutionResponse;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Interface holds method to run the scripts asynchronously
 * @author Mohammad Uzair
 */
public interface AsyncScriptExecutionEngine {

    CompletableFuture<ScriptExecutionResponse> run(String script, String fileName, String... args) throws IOException;

}
