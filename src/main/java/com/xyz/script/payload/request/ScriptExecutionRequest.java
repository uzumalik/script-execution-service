package com.xyz.script.payload.request;

import com.xyz.script.common.ScriptType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("Model for script execution request")
@Getter
@Setter
public class ScriptExecutionRequest {

    @ApiModelProperty(example = "GROOVY")
    @NotNull
    private ScriptType scriptType;

    @ApiModelProperty(example = "return 2 * 2")
    @NotEmpty
    private String script;

    @ApiModelProperty(notes = "command line arguments")
    private String[] args;

}
