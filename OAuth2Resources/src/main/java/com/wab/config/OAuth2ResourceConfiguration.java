package com.wab.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@SuppressWarnings("SpringJavaAutowiringInspection")
public class OAuth2ResourceConfiguration extends ResourceServerConfigurerAdapter {
    @Autowired
    TokenStore tokenStore;

    Logger log = LoggerFactory.getLogger(OAuth2ResourceConfiguration.class);

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        log.info("Configuring ResourceServerSecurityConfigurer ");
        resources.resourceId("appClient2").tokenStore(tokenStore);
    }

    /**
     *
     * You need a WebSecurityConfigurerAdapter to secure the /authorize endpoint and to provide a way for users to authenticate.
     * AppUser Spring Boot application would do that for you (by adding its own WebSecurityConfigurerAdapter with HTTP basic auth).
     * It creates a filter chain with order=0 by default, and protects all resources unless you provide a request matcher.
     * The @EnableResourceServer does something similar, but the filter chain it adds is at order=3 by default,
     * so it is a catch-all fallback for your own WebSecurityConfigurerAdapter at order=0.
     * Your configuration looks sane (the login chain takes precedence, but only matches a small set of requests).
     *  回答者：Dave Syer
     * Yes. Maybe read this: spring.io/guides/topicals/spring-security-architecture. – Dave Syer
     *
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/system/**").hasAuthority("ADMIN")
                .antMatchers("/public/**").permitAll();
    }
}
