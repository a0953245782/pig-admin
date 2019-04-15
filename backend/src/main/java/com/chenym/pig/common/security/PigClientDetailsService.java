package com.chenym.pig.common.security;

import com.chenym.pig.common.constant.SecurityConstants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

public class PigClientDetailsService extends JdbcClientDetailsService {
    public PigClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Cacheable(value = SecurityConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        return super.loadClientByClientId(clientId);
    }
}
