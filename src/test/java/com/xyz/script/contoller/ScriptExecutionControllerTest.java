package com.xyz.script.contoller;


import com.google.gson.Gson;
import com.xyz.script.controller.ScriptExecutionController;
import com.xyz.script.exception.ExceptionReason;
import com.xyz.script.exception.ServiceException;
import com.xyz.script.helper.PayloadHelper;
import com.xyz.script.payload.request.ScriptExecutionRequest;
import com.xyz.script.payload.response.ScriptExecutionResponse;
import com.xyz.script.service.impl.ScriptExecutionServiceImpl;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;
/**
 * Test class for {@link com.xyz.script.controller.ScriptExecutionController}
 * @author Mohammad Uzair
 */
@RunWith(MockitoJUnitRunner.class)
// @WebAppConfiguration
public class ScriptExecutionControllerTest {

    private static final String SCRIPT_EXECUTION_PATH = "/execution";
    @InjectMocks
    private ScriptExecutionController controller;

    @Mock
    private ScriptExecutionServiceImpl service;

    private MockMvc mockMvc;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }

    @Test
    public void testExecuteScript() throws Exception{

        Gson gson = new Gson();
        ScriptExecutionResponse response = PayloadHelper.mockScriptExecutionResponse();
        when(service.executeScript(any(ScriptExecutionRequest.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post(SCRIPT_EXECUTION_PATH)
                .content(gson.toJson(PayloadHelper.mockScriptExecutionRequest()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("executionResult", is(response.getExecutionResult())))
                .andExpect(jsonPath("prints", is(response.getPrints())));

    }

    @Test
    public void testExecuteScript_badRequest() throws Exception{

        Gson gson = new Gson();
        ScriptExecutionResponse response = PayloadHelper.mockScriptExecutionResponse();
        ScriptExecutionRequest request = PayloadHelper.mockScriptExecutionRequest();
        request.setScript(null);
        when(service.executeScript(any(ScriptExecutionRequest.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post(SCRIPT_EXECUTION_PATH)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));


    }


}
