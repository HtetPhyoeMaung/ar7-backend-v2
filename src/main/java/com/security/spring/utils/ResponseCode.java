package com.security.spring.utils;

public class ResponseCode {

    private ResponseCode(){

    }
    public static final int SUCCESS = 0;
    public static final int INTERNAL_SERVER_ERROR = 999;
    public static final int  API_MEMBER_DOES_NOT_EXIST = 1000;
    public static final int  API_MEMBER_BALANCE_IS_INSUFFICIENT = 1001;
    public static final int  API_PROXY_KEY_ERROR = 1002;
    public static final int  DUPLICATE_API_TRANSACTIONS = 1003;
    public static final int  API_SIGNATURE_IS_INVALID = 1004;
    public static final int  API_NOT_GETTING_GAME_LIST = 1005;
    public static final int  API_BET_NOT_EXISTS = 1006;



}
