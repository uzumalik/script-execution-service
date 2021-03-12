package com.xyz.script.payload.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Mohammad Uzair
 */

@ApiModel("Model for Script execution response")
@Getter
@Setter
@Builder
public class ScriptExecutionResponse implements Serializable {


    private static final long serialVersionUID = 5755359415053534344L;

    @ApiModelProperty(notes = "this contains results of execution if script return something")
    private String executionResult;

    @ApiModelProperty(notes = "this contains prints if script printing anything on console")
    private String prints;

}
