package com.xyz.script.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(description = "Error response model fro ScriptExecutionService")
@NoArgsConstructor
public class ExceptionResponseModel {

    public ExceptionResponseModel(ExceptionReason reason, String desc){
        this.code = reason;
        this.desc = desc;
        this.timestamp = LocalDateTime.now();

    }

    @ApiModelProperty
    private ExceptionReason code;

    @ApiModelProperty
    private String desc;

    @ApiModelProperty
    List<FieldError> fieldErrors;

    @ApiModelProperty
    private LocalDateTime timestamp = LocalDateTime.now();


}
