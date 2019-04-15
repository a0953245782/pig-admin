package com.chenym.pig.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenym.pig.common.constant.CommonConstants;
import com.chenym.pig.dto.UserDTO;
import com.chenym.pig.dto.UserInfo;
import com.chenym.pig.mapper.SysUserMapper;
import com.chenym.pig.model.SysRole;
import com.chenym.pig.model.SysUser;
import com.chenym.pig.model.SysUserRole;
import com.chenym.pig.service.SysMenuService;
import com.chenym.pig.service.SysRoleService;
import com.chenym.pig.service.SysUserRoleService;
import com.chenym.pig.service.SysUserService;
import com.chenym.pig.utils.R;
import com.chenym.pig.vo.MenuVO;
import com.chenym.pig.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final SysRoleService sysRoleService;
    private final SysMenuService sysMenuService;
    private final SysUserRoleService sysUserRoleService;

    /**
     * 通过查用户的全部信息
     *
     * @param sysUser 用户
     * @return
     */
    @Override
    public UserInfo findUserInfo(SysUser sysUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //设置角色列表  （ID）
        List<Integer> roleIds = sysRoleService.findRolesByUserId(sysUser.getUserId())
                .stream()
                .map(SysRole::getRoleId)
                .collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Integer.class));

        //设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<String> permissionList = sysMenuService.findMenuByRoleId(roleId)
                    .stream()
                    .filter(menuVo -> StringUtils.isNotEmpty(menuVo.getPermission()))
                    .map(MenuVO::getPermission)
                    .collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }

    @Override
    @CacheEvict(value = "user_details", key = "#userDto.username")
    public R<Boolean> updateUserInfo(UserDTO userDto) {
        UserVO userVO = baseMapper.getUserVoByUsername(userDto.getUsername());
        SysUser sysUser = new SysUser();
        if (StrUtil.isNotBlank(userDto.getPassword())
            && StrUtil.isNotBlank(userDto.getNewpassword1())) {
            if (ENCODER.matches(userDto.getPassword(), userVO.getPassword())) {
                sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
            } else {
                log.warn("原密码错误，修改密码失败:{}", userDto.getUsername());
                return new R<>(Boolean.FALSE, "原密码错误，修改失败");
            }
        }
        sysUser.setPhone(userDto.getPhone());
        sysUser.setUserId(userVO.getUserId());
        sysUser.setAvatar(userDto.getAvatar());

        return new R<>(this.updateById(sysUser));
    }

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDTO 参数列表
     * @return
     */
    @Override
    public IPage getUsersWithRolePage(Page page, UserDTO userDTO) {
        return baseMapper.getUserVosPage(page, userDTO);
    }

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return Boolean
     */
    @Override
    @CacheEvict(value = "user_details", key = "#sysUser.username")
    public Boolean deleteUserById(SysUser sysUser) {
        sysUserRoleService.deleteByUserId(sysUser.getUserId());
        this.removeById(sysUser.getUserId());
        return Boolean.TRUE;
    }

    /**
     * 保存用户信息
     *
     * @param userDto DTO 对象
     * @return success/fail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        baseMapper.insert(sysUser);
        List<SysUserRole> userRoleList = userDto.getRole()
                .stream().map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(sysUser.getUserId());
                    userRole.setRoleId(roleId);
                    return userRole;
                }).collect(Collectors.toList());
        return sysUserRoleService.saveBatch(userRoleList);
    }

    @Override
    @CacheEvict(value = "user_details", key = "#userDto.username")
    public Boolean updateUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());

        if (StrUtil.isNotBlank(userDto.getPassword())) {
            sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        }
        this.updateById(sysUser);

        sysUserRoleService.remove(Wrappers.<SysUserRole>update().lambda()
                .eq(SysUserRole::getUserId, userDto.getUserId()));
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });
        return Boolean.TRUE;
    }
}
