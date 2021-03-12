package com.xyz.script.controller;

import com.xyz.script.payload.request.ScriptExecutionRequest;
import com.xyz.script.payload.response.ScriptExecutionResponse;
import com.xyz.script.service.ScriptExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * This controller class is responsible to provide APIs
 * for script execution
 * @author Mohammad Uzair
 */

@RestController
@RequestMapping()
public class ScriptExecutionController {

    @Autowired
    private ScriptExecutionService executionService;

    @PostMapping("/execution")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ScriptExecutionResponse> executeScript(@RequestBody @Valid ScriptExecutionRequest request) throws Exception {

        return ResponseEntity.ok(executionService.executeScript(request));

    }
}
