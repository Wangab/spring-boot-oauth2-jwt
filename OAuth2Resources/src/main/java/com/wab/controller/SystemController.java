package com.wab.controller;

import com.wab.RESTResponse;
import com.wab.model.entity.AppBaseClientDetaile;
import com.wab.model.entity.AppUser;
import com.wab.model.service.inf.AppClientDetailService;
import com.wab.model.service.inf.AppUserService;
import com.wab.service.SystemUserService;
import com.wab.vo.APPUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/system")
public class SystemController {
    Logger log = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private AppClientDetailService appClientDetailService;


    @RequestMapping(
            path = "/users/{uid}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Map<String, Object>> readUser(@PathVariable("uid") String uid) {
        ResponseEntity<Map<String, Object>> result = null;
        try {
            AppUser user = appUserService.getAppUserByUsername(uid);
            result = RESTResponse.getResponse(HttpStatus.OK, user);
        } catch (Exception e) {
            e.printStackTrace();
            result = RESTResponse.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
        return result;
    }

    @RequestMapping(
            path = "/users",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Map<String, Object>> getAllUsers(){
        ResponseEntity<Map<String, Object>> result = null;
        try {
            result = RESTResponse.getResponse(HttpStatus.OK, systemUserService.getAllUsers());
        } catch (Exception e) {
            log.error(e.getMessage(),e.getCause());
            result = RESTResponse.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
        return result;
    }

    @RequestMapping(
            path = "/{clientId}/users",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Map<String, Object>> addUser(@PathVariable("clientId") String clientID, @RequestBody @Valid APPUserVO appUserVO, BindingResult vaildResult) {
        ResponseEntity<Map<String, Object>> result = null;
        if (vaildResult.hasErrors()) {
            StringBuffer sb = new StringBuffer();
            for (ObjectError error: vaildResult.getAllErrors()) {
                sb.append("[");
                sb.append(error.getDefaultMessage());
                sb.append("]");
            }
            result = RESTResponse.getErrorResponse(HttpStatus.BAD_REQUEST, sb.toString());
        } else {
            try {
                String userID = systemUserService.saveSystemUser(clientID, appUserVO);
                result = RESTResponse.getResponse(HttpStatus.OK, userID);
            } catch (Exception e) {
                e.printStackTrace();
                result = RESTResponse.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
        return result;
    }

    @RequestMapping(
            path = "/apps/{cid}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Map<String, Object>> getClientInfomations(@PathVariable("cid") String cid){
        ResponseEntity<Map<String, Object>> result = null;
        AppBaseClientDetaile appBaseClientDetaile = appClientDetailService.getBaseClientDetaileByID(cid);
        if (appBaseClientDetaile == null){
            return RESTResponse.getErrorResponse(HttpStatus.NOT_FOUND, cid + " Not Found");
        }else {
            return RESTResponse.getResponse(HttpStatus.OK, appBaseClientDetaile);
        }
    }
}
