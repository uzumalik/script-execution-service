package com.xyz.script.exception;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FieldError implements Serializable {


    private static final long serialVersionUID = 9165941812794654513L;
    @Expose
    private String path;

    @Expose
    private String constraint;
}
