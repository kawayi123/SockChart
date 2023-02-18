package com.hash.socettestdemo.utils;

/**
 * Constants files
 * create by duxl 2021/2/20
 */
public interface Constants {

    /*** socket address */
    String SOCKET_SERVER = "";
//rrr
    /*** 接口响应code */
    interface HttpResponseCode {
        int DEFAULT = 0; // success
        int APP_USER_LOGOUT = 10; // This account has already been logged in on another device, please log in again
        int AUTHORIZATION_INVALID = 11; // Authorization code invalid (need to re-authorize)
        int AUTHORIZATION_NONE = 12; //Authorization code invalid (need to re-authorize)
    }
}
