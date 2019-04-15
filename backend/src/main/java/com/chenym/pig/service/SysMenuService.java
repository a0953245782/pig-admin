package com.chenym.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenym.pig.model.SysMenu;
import com.chenym.pig.utils.R;
import com.chenym.pig.vo.MenuVO;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    List<MenuVO> findMenuByRoleId(Integer roleId);

    /**
     * 级联删除菜单
     *
     * @param id 菜单ID
     * @return 成功、失败
     */
    R removeMenuById(Integer id);

    /**
     * 更新菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 成功、失败
     */
    Boolean updateMenuById(SysMenu sysMenu);

}
