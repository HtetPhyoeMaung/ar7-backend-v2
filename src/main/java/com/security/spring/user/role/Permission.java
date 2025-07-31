package com.security.spring.user.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    SENIORMASTER_READ("seniormaster:read"),
    SENIORMASTER_UPDATE("seniormaster:update"),
    SENIORMASTER_CREATE("seniormaster:create"),
    SENIORMASTER_DELETE("seniormaster:delete"),

    MASTER_READ("master:read"),
    MASTER_UPDATE("master:update"),
    MASTER_CREATE("master:create"),
    MASTER_DELETE("master:delete"),

    AGENT_READ("agent:read"),
    AGENT_UPDATE("agent:update"),
    AGENT_CREATE("agent:create"),
    AGENT_DELETE("agent:delete"),

    AFFILIATEAGENT_READ("affiliateagent:read"),
    AFFILIATEAGENT_UPDATE("affiliateagent:update"),
    AFFILIATEAGENT_CREATE("affiliateagent:create"),
    AFFILIATEAGENT_DELETE("affiliateagent:delete"),

    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete");

    @Getter
    private final String permission;
}
