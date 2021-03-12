package com.xyz.script.helper;

import com.xyz.script.common.ScriptType;
import com.xyz.script.payload.request.ScriptExecutionRequest;
import com.xyz.script.payload.response.ScriptExecutionResponse;

public class PayloadHelper {

    public static ScriptExecutionResponse mockScriptExecutionResponse() {

       return ScriptExecutionResponse.builder().executionResult("4").prints("Hello World!!").build();
    }

    public static ScriptExecutionRequest mockScriptExecutionRequest() {
        ScriptExecutionRequest request = new ScriptExecutionRequest();
        request.setScript("println(\"Hello World!!\");return 2 * 2");
        request.setScriptType(ScriptType.GROOVY);
        return request;
    }
}
