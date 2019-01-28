package com.wab.model.service.impl;

import com.wab.model.dao.AppUserRepository;
import com.wab.model.entity.AppUser;
import com.wab.model.service.inf.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public AppUser saveAppUser(AppUser user) {
        return appUserRepository.save(user);
    }

    @Override
    public AppUser getAppUserByUsername(String username) {
        Optional<AppUser> result = appUserRepository.findById(username);
        return result.orElse(null);
    }

    @Override
    public Collection<AppUser> getAllusers() {
        return appUserRepository.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "creationDate")));
    }
}
