
package com.chenym.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenym.pig.model.SysLog;
import com.chenym.pig.vo.PreLogVo;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author lengleng
 * @since 2019/2/1
 */
public interface SysLogService extends IService<SysLog> {


	/**
	 * 批量插入前端错误日志
	 *
	 * @param preLogVoList 日志信息
	 * @return true/false
	 */
	Boolean saveBatchLogs(List<PreLogVo> preLogVoList);
}
