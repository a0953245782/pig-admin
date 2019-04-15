package com.chenym.pig.web;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chenym.pig.common.constant.CommonConstants;
import com.chenym.pig.common.log.annotation.SysLog;
import com.chenym.pig.dto.MenuTree;
import com.chenym.pig.dto.TreeUtil;
import com.chenym.pig.model.SysMenu;
import com.chenym.pig.service.SysMenuService;
import com.chenym.pig.utils.R;
import com.chenym.pig.utils.SecurityUtils;
import com.chenym.pig.vo.MenuVO;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
@AllArgsConstructor
public class MenuController {

    private final SysMenuService sysMenuService;

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Integer id) {
        return new R<>(sysMenuService.getById(id));
    }

    /**
     * 返回角色的菜单集合
     *
     * @param roleId 角色ID
     * @return 属性集合
     */
    @GetMapping("/tree/{roleId}")
    public List getRoleTree(@PathVariable Integer roleId) {
        return sysMenuService.findMenuByRoleId(roleId)
                .stream()
                .map(MenuVO::getMenuId)
                .collect(Collectors.toList());
    }

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping
    public R getUserMenu() {
        // 获取符合条件的菜单
        Set<MenuVO> all = new HashSet<>();
        SecurityUtils.getRoles()
                .forEach(roleId -> all.addAll(sysMenuService.findMenuByRoleId(roleId)));
        List<MenuTree> menuTreeList = all.stream()
                .filter(menuVo -> CommonConstants.MENU.equals(menuVo.getType()))
                .map(MenuTree::new)
                .sorted(Comparator.comparingInt(MenuTree::getSort))
                .collect(Collectors.toList());
        return new R<>(TreeUtil.bulid(menuTreeList, -1));
    }

    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/tree")
    public R getTree() {
        return new R<>(TreeUtil.bulidATree(sysMenuService.list(Wrappers.emptyWrapper()), -1));
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return success/false
     */
    @SysLog("新增菜单")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_add')")
    public R save(@Valid @RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.save(sysMenu));
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return success/false
     */
    @SysLog("删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_menu_del')")
    public R removeById(@PathVariable Integer id) {
        return sysMenuService.removeMenuById(id);
    }


    /**
     * 更新菜单
     *
     * @param sysMenu
     * @return
     */
    @SysLog("更新菜单")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_edit')")
    public R update(@Valid @RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.updateMenuById(sysMenu));
    }


}
