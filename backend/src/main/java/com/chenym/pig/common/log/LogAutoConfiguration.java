package com.chenym.pig.common.log;

import com.chenym.pig.common.log.aspect.SysLogAspect;
import com.chenym.pig.common.log.event.SysLogListener;
import com.chenym.pig.service.SysLogService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogAutoConfiguration {
    private final SysLogService sysLogService;

    @Bean
    public SysLogListener sysLogListener() {
        return new SysLogListener(sysLogService);
    }

    @Bean
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }
}
