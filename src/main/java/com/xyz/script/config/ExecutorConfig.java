package com.xyz.script.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.concurrent.Executor;

/**
 * Provide custom implementation for {@link Executor} instance to support async processing.
 * {@link EnableAsync#mode()} is set to default {@link AdviceMode#PROXY},
 * which allows methods to be intercepted on instance calls, methods on this(same class) will not be intercepted
 * @author Mohammad Uzair
 */

@EnableAsync(mode = AdviceMode.PROXY)
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "executor")
@Getter
@Setter
@Validated
public class ExecutorConfig  implements AsyncConfigurer{

    @Positive
    private int corePoolSize;

    @Positive
    private int maxPoolSize;

    @Positive
    private int queueCapacity;

    @Positive
    private int keepAliveSeconds;

    @NotEmpty
    private String threadGroupName;

    @NotEmpty
    private String threadNamePrefix;

    @Lazy
    @Autowired
    private TaskDecorator taskDecorator;

    @Lazy
    @Autowired
    private AsyncUncaughtExceptionHandler exceptionHandler;


    @Override
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadGroupName(threadGroupName);
        executor.setThreadNamePrefix(threadNamePrefix);

        executor.setTaskDecorator(taskDecorator);

        executor.initialize();
        return executor;
    }


    /**
     * The {@link AsyncUncaughtExceptionHandler} instance to be used
     * when an exception is thrown during an asynchronous method execution
     * with {@code void} return type.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return exceptionHandler;
    }
}
