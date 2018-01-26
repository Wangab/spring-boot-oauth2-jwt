package com.wab.config;

import com.wab.model.conf.SystemConfig;
import com.wab.model.entity.AppBaseClientDetaile;
import com.wab.service.ApplicationClientDetailsService;
import com.wab.service.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
@SuppressWarnings("SpringJavaAutowiringInspection")
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private ApplicationClientDetailsService clientDetailsService;
    @Autowired
    private ApplicationUserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        AppBaseClientDetaile baseClientDetaile = new AppBaseClientDetaile();
        baseClientDetaile.setClientId(SystemConfig.SYS_CLIENT_ID);
        baseClientDetaile.setName("system_app");
        baseClientDetaile.setDescription("系统所用根租户");
        baseClientDetaile.setClientSecret(SystemConfig.SYS_CLIENT_SECRET);
        baseClientDetaile.setScope(Arrays.asList("openid"));
        baseClientDetaile.setAuthorities(
                Arrays.asList(
                        new SimpleGrantedAuthority(SystemConfig.ROLE_ADMIN),
                        new SimpleGrantedAuthority(SystemConfig.ROLE_USER),
                        new SimpleGrantedAuthority(SystemConfig.ROLE_SYSTEM)
                )
        );
        baseClientDetaile.setAuthorizedGrantTypes(Arrays.asList(
                SystemConfig.GRANT_TYPE_AUTHORIZATION,
                SystemConfig.GRANT_TYPE_CLIENT_CREDENTITALS,
                SystemConfig.GRANT_TYPE_IMPLICIT,
                SystemConfig.GRANT_TYPE_PASSWORD,
                SystemConfig.GRANT_TYPE_REFRESH));
        clientDetailsService.saveClientDetails(baseClientDetaile);
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(jwtTokenEnhancer())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()");
        super.configure(security);
    }

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        // mySecretKey为产生秘钥文件jwt.jks时所使用的密码
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
        return converter;
    }
}
