package com.wab.controller;

import com.wab.RESTResponse;
import com.wab.model.entity.SimpleUserInfo;
import com.wab.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wanganbang
 * <p>
 * PublicController Creatd on 2018/1/8
 */
@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private SystemUserService systemUserService;

    @RequestMapping(
            path = "/customer/{clientId}/{uid}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Map<String, Object>> getRandomOnlineUser(@PathVariable("clientId") String client_id, @PathVariable("uid") String uid){
        SimpleUserInfo simpleUserInfo = systemUserService.getCustomerFromCache(client_id,uid);
        return RESTResponse.getResponse(HttpStatus.OK, simpleUserInfo);
    }

}
