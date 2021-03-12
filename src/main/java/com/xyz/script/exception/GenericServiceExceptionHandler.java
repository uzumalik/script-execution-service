package com.xyz.script.exception;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mohammad Uzair
 *
 * This class is to handle all script service exception and cast them into meaningful exceptions for the consumers
 */

@ControllerAdvice
@Slf4j
public class GenericServiceExceptionHandler implements AsyncUncaughtExceptionHandler {


    private static final String INVALID_REQUEST = "Invalid Request";

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResponseModel> handleScriptServiceException(ServiceException ex){

        log.error("Caught Script Service Exception, exception", ex);

        ExceptionResponseModel response = new ExceptionResponseModel();
        response.setCode(ex.getReason());
        response.setDesc(ex.getMessage());

        return new ResponseEntity<>(response, response.getCode().getHttpStatus());


    }

    /**
     * Handle the given uncaught exception thrown from an asynchronous method.
     *
     * @param ex     the exception thrown from the asynchronous method
     * @param method the asynchronous method
     * @param params the parameters used to invoked the method
     */
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Async exception occurred, method {}, params {} ", method, params);
        handleUncaughtException(ex);
    }


    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ExceptionResponseModel> handleUncaughtException(Throwable ex){
        log.error("Unexpected exception occurred", ex);
        return new ResponseEntity<>(new ExceptionResponseModel(ExceptionReason.UNKNOWN_EXCEPTION,"Something went wrong"), ExceptionReason.UNKNOWN_EXCEPTION.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponseModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        log.info("got constraint violations");
       List<FieldError> errors =  ex.getBindingResult().getAllErrors().stream()
                .filter(err -> err instanceof org.springframework.validation.FieldError)
                .map(err -> (org.springframework.validation.FieldError)err)
                .map(this::cast)
                .collect(Collectors.toList());
       return buildErrorResponse(errors);

    }

    private ResponseEntity<ExceptionResponseModel> buildErrorResponse(List<FieldError> errors) {

        ExceptionResponseModel responseModel = new ExceptionResponseModel();
        responseModel.setCode(ExceptionReason.BAD_REQUEST);
        responseModel.setDesc(INVALID_REQUEST);
        responseModel.setFieldErrors(errors);
        return new ResponseEntity<>(responseModel, new HttpHeaders(), responseModel.getCode().getHttpStatus());

    }

    private FieldError cast(org.springframework.validation.FieldError fieldError) {
        FieldError error = new FieldError();
        error.setPath(fieldError.getField());
        error.setConstraint(fieldError.getCode());
        return error;
    }
}
