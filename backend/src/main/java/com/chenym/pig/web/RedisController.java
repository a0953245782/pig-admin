package com.chenym.pig.web;


import com.chenym.pig.service.RedisService;
import com.chenym.pig.utils.R;
import com.chenym.pig.vo.RedisInfoVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/redis")
@AllArgsConstructor
public class RedisController {
	
    @Autowired
    private RedisService redisService;
	
    @GetMapping("info")
    public R getRedisInfo() throws Exception {
        List<RedisInfoVo> infoList = this.redisService.getRedisInfo();
        return new R<>(infoList);
    }

    @GetMapping("keysSize")
    public R getKeysSize() throws Exception {
        return new R<>(redisService.getKeysSize());
    }

    @GetMapping("memoryInfo")
    public R getMemoryInfo() throws Exception {
        return new R<>(redisService.getMemoryInfo());
    }
}