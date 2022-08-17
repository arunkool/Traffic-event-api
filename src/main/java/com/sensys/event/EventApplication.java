package com.sensys.event;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.gemfire.config.annotation.*;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.geode.config.annotation.EnableClusterAware;
import org.springframework.geode.config.annotation.UseMemberName;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static org.springframework.data.gemfire.config.annotation.EnableSsl.Component.*;


@EnablePdx
@SpringBootApplication
@ClientCacheApplication(subscriptionEnabled = true)
@EnableEntityDefinedRegions(basePackages = "com.sensys.event.entity")
@EnableGemfireRepositories(basePackages = "com.sensys.event.repository")
@ComponentScan(basePackages = {"com.sensys.event.controller","com.sensys.event.service","com.sensys.event.advice" })
@EnableClusterConfiguration(useHttp = true, requireHttps=false)
@OpenAPIDefinition(info = @Info(title="Traffic Event API",version="1.0",description = "Store and Process traffic events"))
@EnableAsync
@UseMemberName("EventApplication")
@EnableClusterAware
public class EventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventApplication.class, args);
    }

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Async Thread-");
        executor.initialize();
        return executor;
    }

}
