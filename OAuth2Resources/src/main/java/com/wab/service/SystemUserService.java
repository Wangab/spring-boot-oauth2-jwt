package com.wab.service;

import com.fasterxml.jackson.core.json.JsonGeneratorImpl;
import com.wab.controller.SystemController;
import com.wab.exception.UserAlreadyExistsException;
import com.wab.model.entity.AppUser;
import com.wab.model.entity.SimpleUserInfo;
import com.wab.model.service.impl.AppUserServiceImpl;
import com.wab.vo.APPUserVO;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class SystemUserService {
    private static Logger LOG = LoggerFactory.getLogger(SystemController.class);
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AppUserServiceImpl appUserService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;// Warning: RedisTemplate can not use wildcard(*), but StringRedisTemplate can do it

    public String saveSystemUser(String client_id, APPUserVO userVO) throws UserAlreadyExistsException {
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        String username = client_id + "_" + userVO.getUsername();
        if (appUserService.getAppUserByUsername(username) != null) {
            throw new UserAlreadyExistsException();
        }
        AppUser user = new AppUser(client_id, username, passwordEncoder.encode(userVO.getPassword()), authorities);
        user.setNick(userVO.getNick());
        if (userVO.getAvatar() != null) {
            user.setAvatar(userVO.getAvatar());
        }
        return appUserService.saveAppUser(user).getUsername();
    }

    public Collection<AppUser> getAllUsers() throws Exception {
        return appUserService.getAllusers();
    }

    public boolean isOnline(String username) {
        String[] user_str = username.split("_");
        if (user_str.length > 1) {
            return stringRedisTemplate.hasKey("online_users:" + user_str[0] + ":" + user_str[1]).booleanValue();
        } else {
            return stringRedisTemplate.hasKey("online_guest_users:" + username).booleanValue();
        }
    }

    public SimpleUserInfo getCustomerFromCache(String client_id, String uid){
        Object object = stringRedisTemplate.opsForHash().get("customer:" + client_id + ":" + uid, "info");
        if (object != null){
            try {
                SimpleUserInfo simpleUserInfo = objectMapper.readValue(object.toString(), SimpleUserInfo.class);
                if (isOnline(simpleUserInfo.getUsername())) {
                    return simpleUserInfo;
                }
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
        return getRandomUserFromApplcations(client_id, uid);
    }

    public SimpleUserInfo getRandomUserFromApplcations(String app_id, String uid) {
        Set<String> keys = stringRedisTemplate.keys("online_users:" + app_id + ":*");
        if (keys != null && !keys.isEmpty()){
            int size = keys.size();
            int index = new Random().nextInt(size);
            String randomKey = keys.toArray()[index].toString();
            AppUser user = appUserService.getAppUserByUsername(app_id + "_" + randomKey.split(":")[2]);
            if (user != null){
                SimpleUserInfo simpleUserInfo = new SimpleUserInfo();
                simpleUserInfo.setAvatar(user.getAvatar());
                simpleUserInfo.setClientId(user.getClientId());
                simpleUserInfo.setCreationDate(user.getCreationDate());
                simpleUserInfo.setEmail(user.getEmail());
                simpleUserInfo.setUsername(user.getUsername());
                try {
                    stringRedisTemplate.opsForHash().put("customer:" + app_id + ":" + uid, "info", objectMapper.writeValueAsString(simpleUserInfo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return simpleUserInfo;
            }
        }
        return null;
    }
}
