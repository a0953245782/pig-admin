package com.chenym.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenym.pig.model.SysUserRole;

public interface SysUserRoleService extends IService<SysUserRole> {
    /**
     * 根据用户Id删除该用户的角色关系
     *
     * @param userId 用户ID
     * @return boolean
     * @author 寻欢·李
     * @date 2017年12月7日 16:31:38
     */
    Boolean deleteByUserId(Integer userId);
}
