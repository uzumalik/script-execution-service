package com.xyz.script.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

/**
 * @author Mohammad Uzair
 */
@Component
@Slf4j
public class LoggingTaskDecorator implements TaskDecorator {

    @Autowired
    private AsyncUncaughtExceptionHandler exceptionHandler;

    /**
     * Decorate the given {@code Runnable}, returning a potentially wrapped
     * {@code Runnable} for actual execution, internally delegating to the
     * original {@link Runnable#run()} implementation.
     *
     * @param runnable the original {@code Runnable}
     * @return the decorated {@code Runnable}
     */
    @Override
    public Runnable decorate(Runnable runnable) {
        return () -> {
            try{
                long startedAt = System.currentTimeMillis();
                runnable.run();
                log.info("Thread {} took {} milliseconds to complete",
                        Thread.currentThread().getName(), System.currentTimeMillis() - startedAt );

            } catch (Exception ex ){
                exceptionHandler.handleUncaughtException(ex, runnable.getClass().getEnclosingMethod(), (Object) null);
            }
        };
    }
}
