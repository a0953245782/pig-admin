package com.chenym.pig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenym.pig.model.SysRole;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    List<SysRole> listRolesByUserId(Integer userId);
}
