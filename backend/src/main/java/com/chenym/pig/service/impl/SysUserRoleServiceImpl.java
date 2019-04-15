package com.chenym.pig.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenym.pig.mapper.SysUserRoleMapper;
import com.chenym.pig.model.SysUserRole;
import com.chenym.pig.service.SysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
    /**
     * 根据用户Id删除该用户的角色关系
     *
     * @param userId 用户ID
     * @return boolean
     * @author 寻欢·李
     * @date 2017年12月7日 16:31:38
     */
    @Override
    public Boolean deleteByUserId(Integer userId) {
        return baseMapper.deleteByUserId(userId);
    }
}
