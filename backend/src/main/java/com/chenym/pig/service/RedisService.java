package com.chenym.pig.service;


import com.chenym.pig.common.exception.RedisConnectException;
import com.chenym.pig.vo.RedisInfoVo;

import java.util.List;
import java.util.Map;

public interface RedisService {

    /**
     * 获取 redis 的详细信息
     *
     * @return List
     */
    List<RedisInfoVo> getRedisInfo() throws RedisConnectException;

    /**
     * 获取 redis key 数量
     *
     * @return Map
     */
    Map<String, Object> getKeysSize() throws RedisConnectException;

    /**
     * 获取 redis 内存信息
     *
     * @return Map
     */
    Map<String, Object> getMemoryInfo() throws RedisConnectException;

}
