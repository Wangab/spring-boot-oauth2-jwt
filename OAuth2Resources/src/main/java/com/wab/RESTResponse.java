package com.wab;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class RESTResponse {
    public static ResponseEntity<Map<String, Object>> getResponse(HttpStatus status, Object data){
        Map<String, Object> map = new HashMap<>();
        map.put("code", status.value());
        map.put("data", data);
        return new ResponseEntity<Map<String, Object>>(map, status);
    }
    public static ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, Object data){
        Map<String, Object> map = new HashMap<>();
        map.put("code", status.value());
        map.put("error", data);
        return new ResponseEntity<Map<String, Object>>(map, status);
    }
    public static ResponseEntity<Map<String, Object>> getSuccessResponse(){
        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpStatus.OK.value());
        map.put("data", "Success");
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
    }
}
