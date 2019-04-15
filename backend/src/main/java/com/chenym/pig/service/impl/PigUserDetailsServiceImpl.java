package com.chenym.pig.service.impl;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.chenym.pig.common.constant.CommonConstants;
import com.chenym.pig.common.constant.SecurityConstants;
import com.chenym.pig.dto.UserInfo;
import com.chenym.pig.model.PigUser;
import com.chenym.pig.model.SysUser;
import com.chenym.pig.service.SysUserService;
import com.chenym.pig.utils.R;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PigUserDetailsServiceImpl implements UserDetailsService {
    private final CacheManager cacheManager;
    private final SysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cache cache = cacheManager.getCache("user_details");
        if (cache != null && cache.get(username) != null) {
            return (PigUser) cache.get(username).get();
        }
        SysUser user = userService.getOne(Wrappers.<SysUser>query()
                .lambda().eq(SysUser::getUsername, username));
        R<UserInfo> result = new R<>(userService.findUserInfo(user));
        UserDetails userDetails = getUserDetails(result);
        cache.put(username, userDetails);
        return userDetails;
    }

    /**
     * 构建userdetails
     *
     * @param result 用户信息
     * @return
     */
    private UserDetails getUserDetails(R<UserInfo> result) {
        if (result == null || result.getData() == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        UserInfo info = result.getData();
        Set<String> dbAuthsSet = new HashSet<>();
        if (ArrayUtil.isNotEmpty(info.getRoles())) {
            // 获取角色
            Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));
        }
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUser user = info.getSysUser();
        // 构造security用户
        return new PigUser(user.getUserId(), user.getDeptId(), user.getUsername(), SecurityConstants.BCRYPT + user.getPassword(),
                StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL), true, true, true, authorities);
    }
}
