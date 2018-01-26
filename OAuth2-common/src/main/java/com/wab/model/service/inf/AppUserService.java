package com.wab.model.service.inf;

import com.wab.model.entity.AppUser;

import java.util.Collection;

public interface AppUserService {
    AppUser saveAppUser(AppUser user);
    AppUser getAppUserByUsername(String username);
    Collection<AppUser> getAllusers();
}
