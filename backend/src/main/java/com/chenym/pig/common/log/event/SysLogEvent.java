package com.chenym.pig.common.log.event;

import com.chenym.pig.model.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * @author lengleng
 * 系统日志事件
 */
public class SysLogEvent extends ApplicationEvent {

	public SysLogEvent(SysLog source) {
		super(source);
	}
}