package com.chenym.pig.service.impl;

import com.chenym.pig.common.exception.RedisConnectException;
import com.chenym.pig.service.RedisService;
import com.chenym.pig.vo.RedisInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<RedisInfoVo> getRedisInfo() throws RedisConnectException {
        Properties info = redisTemplate.getConnectionFactory().getConnection().info();
        List<RedisInfoVo> infoList = new ArrayList<>();
        for (String key : info.stringPropertyNames()) {
            RedisInfoVo redisInfo = new RedisInfoVo();
            redisInfo.setKey(key);
            redisInfo.setValue(info.getProperty(key));
            infoList.add(redisInfo);
        }
        return infoList;
    }

    @Override
    public Map<String, Object> getKeysSize() throws RedisConnectException {
        Long dbSize = redisTemplate.execute(RedisServerCommands::dbSize);
        Map<String, Object> map = new HashMap<>();
        map.put("create_time", System.currentTimeMillis());
        map.put("dbSize", dbSize);
        return map;
    }

    @Override
    public Map<String, Object> getMemoryInfo() throws RedisConnectException {
        Properties info = redisTemplate.getConnectionFactory().getConnection().info();
        Map<String, Object> map = null;
        for (String key : info.stringPropertyNames()) {
            if ("used_memory".equals(key)) {
                map = new HashMap<>();
                map.put("used_memory", info.getProperty(key));
                map.put("create_time", System.currentTimeMillis());
                break;
            }
        }
        return map;
    }

}