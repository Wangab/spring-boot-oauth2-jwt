package com.wab.service;

import com.wab.model.dao.AppUserRepository;
import com.wab.model.entity.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    private static Logger LOG = LoggerFactory.getLogger(ApplicationUserDetailsService.class);
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AppUser user = appUserRepository.findOne(username);
            if (user != null) {
                return user;
            } else {
                throw new UsernameNotFoundException(username + " not found");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new UsernameNotFoundException(username + " not found or service has some error --> " + e.getMessage());
        }
    }

    public UserDetails saveUserDetails(UserDetails userDetails) {
        return appUserRepository.save((AppUser)userDetails);
    }
}
