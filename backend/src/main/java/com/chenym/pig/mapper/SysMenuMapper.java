package com.chenym.pig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenym.pig.model.SysMenu;
import com.chenym.pig.vo.MenuVO;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 通过角色编号查询菜单
     *
     * @param roleId 角色ID
     * @return
     */
    List<MenuVO> listMenusByRoleId(Integer roleId);

}
