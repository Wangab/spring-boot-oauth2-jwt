package com.wab.model.dao;

import com.wab.model.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * AppUserRepository
 *
 * @author Anbang Wang
 * @date 2017/5/22
 */
public interface AppUserRepository extends MongoRepository<AppUser, String> {

}
