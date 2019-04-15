package com.chenym.pig.common.log.aspect;

import com.chenym.pig.common.core.util.SpringContextHolder;
import com.chenym.pig.common.log.annotation.SysLog;
import com.chenym.pig.common.log.event.SysLogEvent;
import com.chenym.pig.common.log.util.SysLogUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 操作日志使用spring event异步入库
 *
 * @author L.cm
 */
@Aspect
@Slf4j
public class SysLogAspect {

	@Around("@annotation(sysLog)")
	public Object around(ProceedingJoinPoint point, SysLog sysLog) throws Throwable {
		String strClassName = point.getTarget().getClass().getName();
		String strMethodName = point.getSignature().getName();
		log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

		com.chenym.pig.model.SysLog logVo = SysLogUtils.getSysLog();
		logVo.setTitle(sysLog.value());
		// 发送异步日志事件
		Long startTime = System.currentTimeMillis();
		Object obj = point.proceed();
		Long endTime = System.currentTimeMillis();
		logVo.setTime(endTime - startTime);
		SpringContextHolder.publishEvent(new SysLogEvent(logVo));
		return obj;
	}

}
