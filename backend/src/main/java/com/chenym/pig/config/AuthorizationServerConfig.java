package com.chenym.pig.config;

import com.chenym.pig.common.constant.SecurityConstants;
import com.chenym.pig.common.security.PigClientDetailsService;
import com.chenym.pig.common.security.PigWebResponseExceptionTranslator;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        PigClientDetailsService clientDetailsService = new PigClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    	 oauthServer
        		.allowFormAuthenticationForClients()
                .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .pathMapping("/oauth/token", "/api/auth/login")
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService)
                .tokenEnhancer(tokenEnhancer())
                .authenticationManager(authenticationManager)
                .reuseRefreshTokens(false)
                .exceptionTranslator(new PigWebResponseExceptionTranslator());
    }

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(SecurityConstants.PROJECT_PREFIX + SecurityConstants.OAUTH_PREFIX);
        return tokenStore;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            final Map<String, Object> additionalInfo = new HashMap<>(4);
            additionalInfo.put("license", SecurityConstants.PROJECT_LICENSE);
            //PigUser pigUser = ((PigUser) authentication.getPrincipal());
            //additionalInfo.put("username", authentication.getName());
            //additionalInfo.put("dept_id", pigUser.getDeptId());
            //additionalInfo.put("user_id", pigUser.getId());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }
}
