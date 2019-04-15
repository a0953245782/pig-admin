package com.chenym.pig.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenym.pig.mapper.SysDictMapper;
import com.chenym.pig.model.SysDict;
import com.chenym.pig.service.SysDictService;
import org.springframework.stereotype.Service;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
}