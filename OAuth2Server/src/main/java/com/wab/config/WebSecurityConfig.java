package com.wab.config;

import com.wab.model.conf.SystemConfig;
import com.wab.model.entity.AppUser;
import com.wab.service.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ApplicationUserDetailsService userDetailsService;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                //.formLogin().and() // 基于Form表单的认证，用户可自定义
                .httpBasic().disable(); // 启用HTTPBasic认证
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(SystemConfig.ROLE_SYSTEM));
        authorities.add(new SimpleGrantedAuthority(SystemConfig.ROLE_ADMIN));
        authorities.add(new SimpleGrantedAuthority(SystemConfig.ROLE_USER));
        AppUser user = new AppUser(SystemConfig.SYS_CLIENT_ID, SystemConfig.SYS_ADMIN_NAME, passwordEncoder.encode(SystemConfig.SYS_ADMIN_PWD), authorities);
        userDetailsService.saveUserDetails(user);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
