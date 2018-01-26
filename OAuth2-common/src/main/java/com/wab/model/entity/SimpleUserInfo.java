package com.wab.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Set;

/**
 * @author wanganbang
 * <p>
 * SimpleUserInfo Creatd on 2018/1/10
 */
public class SimpleUserInfo {
    private String username;
    private String nick;
    private String email;
    private Set<String> avatar;
    private String clientId;
    private Date creationDate;

    public SimpleUserInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getAvatar() {
        return avatar;
    }

    public void setAvatar(Set<String> avatar) {
        this.avatar = avatar;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
