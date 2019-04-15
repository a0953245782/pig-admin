package com.chenym.pig.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenym.pig.mapper.SysRoleMapper;
import com.chenym.pig.mapper.SysRoleMenuMapper;
import com.chenym.pig.model.SysRole;
import com.chenym.pig.model.SysRoleMenu;
import com.chenym.pig.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysRole> findRolesByUserId(Integer userId) {
        return baseMapper.listRolesByUserId(userId);
    }


    /**
     * 通过角色ID，删除角色,并清空角色菜单缓存
     *
     * @param id
     * @return
     */
    @Override
    @CacheEvict(value = "menu_details", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRoleById(Integer id) {
        sysRoleMenuMapper.delete(Wrappers
                .<SysRoleMenu>update().lambda()
                .eq(SysRoleMenu::getRoleId, id));
        return this.removeById(id);
    }
}
