package com.chenym.pig.common.log.event;

import com.chenym.pig.model.SysLog;
import com.chenym.pig.service.SysLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;


/**
 * @author lengleng
 * 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class SysLogListener {
    private final SysLogService sysLogService;

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        SysLog sysLog = (SysLog) event.getSource();
        sysLogService.save(sysLog);
    }
}
