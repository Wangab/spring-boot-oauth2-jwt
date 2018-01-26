package com.wab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;

/**
 * WebSecurityConfiguration
 *
 * @author Anbang Wang
 * @date 2017/5/19
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()//处理跨域请求中的Preflight请求
        .anyRequest().authenticated();
    }
}
