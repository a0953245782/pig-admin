package com.chenym.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenym.pig.model.SysRole;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId
     * @return
     */
    List<SysRole> findRolesByUserId(Integer userId);

    /**
     * 通过角色ID，删除角色
     *
     * @param id
     * @return
     */
    Boolean removeRoleById(Integer id);
}
