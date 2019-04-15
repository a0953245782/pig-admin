package com.chenym.pig.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenym.pig.common.log.annotation.SysLog;
import com.chenym.pig.dto.UserDTO;
import com.chenym.pig.model.SysUser;
import com.chenym.pig.service.SysUserService;
import com.chenym.pig.utils.R;
import com.chenym.pig.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final SysUserService userService;

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    public R info() {
        String username = SecurityUtils.getUser().getUsername();
        SysUser user = userService.getOne(Wrappers.<SysUser>query()
                .lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return new R<>(Boolean.FALSE, "获取当前用户信息失败");
        }
        return new R<>(userService.findUserInfo(user));
    }

    /**
     * 修改个人信息
     *
     * @param userDto userDto
     * @return success/false
     */
    @SysLog("修改个人信息")
    @PutMapping("/edit")
    public R updateUserInfo(@Valid @RequestBody UserDTO userDto) {
        return userService.updateUserInfo(userDto);
    }

    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param userDTO 查询参数列表
     * @return 用户集合
     */
    @GetMapping("/page")
    public R getUserPage(Page page, UserDTO userDTO) {
        return new R<>(userService.getUsersWithRolePage(page, userDTO));
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return R
     */
    @SysLog("删除用户信息")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_user_del')")
    public R userDel(@PathVariable Integer id) {
        SysUser sysUser = userService.getById(id);
        return new R<>(userService.deleteUserById(sysUser));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @SysLog("添加用户")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_user_add')")
    public R user(@RequestBody UserDTO userDto) {
        return new R<>(userService.saveUser(userDto));
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    @SysLog("更新用户信息")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_user_edit')")
    public R updateUser(@Valid @RequestBody UserDTO userDto) {
        return new R<>(userService.updateUser(userDto));
    }
}
