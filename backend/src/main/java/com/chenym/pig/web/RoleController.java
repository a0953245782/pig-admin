package com.chenym.pig.web;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenym.pig.common.log.annotation.SysLog;
import com.chenym.pig.model.SysRole;
import com.chenym.pig.service.SysRoleMenuService;
import com.chenym.pig.service.SysRoleService;
import com.chenym.pig.utils.R;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/role")
public class RoleController {

    private final SysRoleService sysRoleService;
    private final SysRoleMenuService sysRoleMenuService;

    /**
     * 分页查询角色信息
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public R getRolePage(Page page) {
        return new R<>(sysRoleService.page(page, Wrappers.emptyWrapper()));
    }


    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @SysLog("删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_role_del')")
    public R removeById(@PathVariable Integer id) {
        return new R<>(sysRoleService.removeRoleById(id));
    }

    /**
     * 添加角色
     *
     * @param sysRole 角色信息
     * @return success、false
     */
    @SysLog("添加角色")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_role_add')")
    public R save(@Valid @RequestBody SysRole sysRole) {
        return new R<>(sysRoleService.save(sysRole));
    }

    /**
     * 修改角色
     *
     * @param sysRole 角色信息
     * @return success/false
     */
    @SysLog("修改角色")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_role_edit')")
    public R update(@Valid @RequestBody SysRole sysRole) {
        return new R<>(sysRoleService.updateById(sysRole));
    }

    /**
     * 更新角色菜单
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return success、false
     */
    @SysLog("更新角色菜单")
    @PutMapping("/menu")
    @PreAuthorize("@pms.hasPermission('sys_role_perm')")
    public R saveRoleMenus(Integer roleId, @RequestParam(value = "menuIds", required = false) String menuIds) {
        SysRole sysRole = sysRoleService.getById(roleId);
        return new R<>(sysRoleMenuService.saveRoleMenus(sysRole.getRoleCode(), roleId, menuIds));
    }

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    public R listRoles() {
        return new R<>(sysRoleService.list(Wrappers.emptyWrapper()));
    }


}
