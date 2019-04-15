package com.chenym.pig.web;

import cn.hutool.core.util.StrUtil;
import com.chenym.pig.utils.R;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class LogoutController {

    private final TokenStore tokenStore;

    /**
     * 退出token
     *
     * @param authHeader Authorization
     */
    @GetMapping("/logout")
    public R<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StringUtils.hasText(authHeader)) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            if (accessToken == null || StrUtil.isBlank(accessToken.getValue())) {
                return new R<>(false, "退出失败，token 为空");
            }
            tokenStore.removeAccessToken(accessToken);
        }
        return new R<>(Boolean.TRUE);
    }
}
