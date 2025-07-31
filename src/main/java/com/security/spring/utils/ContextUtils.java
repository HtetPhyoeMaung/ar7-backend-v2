package com.security.spring.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class ContextUtils {
    private ContextUtils(){
    }
    public static String getRoleFromContext(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().orElseThrow().getAuthority();
    }
    public static String getAr7IdFromContext(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
