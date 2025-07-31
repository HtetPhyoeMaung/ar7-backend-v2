package com.security.spring.user.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.security.spring.user.role.Permission.*;

@RequiredArgsConstructor
public enum Role {

    USER(
            Set.of(
                    USER_READ,
                    USER_UPDATE,
                    USER_CREATE,
                    USER_DELETE
            )
    ),
    AFFILIATEAGENT(
            Set.of(
                    AFFILIATEAGENT_READ,
                    AFFILIATEAGENT_UPDATE,
                    AFFILIATEAGENT_CREATE,
                    AFFILIATEAGENT_DELETE
            )
    ),
    AGENT(
            Set.of(
                    AGENT_READ,
                    AGENT_UPDATE,
                    AGENT_CREATE,
                    AGENT_DELETE
            )
    ),
    MASTER(
            Set.of(
                    MASTER_READ,
                    MASTER_UPDATE,
                    MASTER_CREATE,
                    MASTER_DELETE
            )
    ),
    SENIORMASTER(
            Set.of(
                    SENIORMASTER_READ,
                    SENIORMASTER_UPDATE,
                    SENIORMASTER_CREATE,
                    SENIORMASTER_DELETE
            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_CREATE,
                    ADMIN_DELETE,

                    SENIORMASTER_READ,
                    SENIORMASTER_UPDATE,
                    SENIORMASTER_CREATE,
                    SENIORMASTER_DELETE,

                    MASTER_READ,
                    MASTER_UPDATE,
                    MASTER_CREATE,
                    MASTER_DELETE,

                    AGENT_READ,
                    AGENT_UPDATE,
                    AGENT_CREATE,
                    AGENT_DELETE,

                    AFFILIATEAGENT_READ,
                    AFFILIATEAGENT_UPDATE,
                    AFFILIATEAGENT_CREATE,
                    AFFILIATEAGENT_DELETE,

                    USER_READ,
                    USER_UPDATE,
                    USER_CREATE,
                    USER_DELETE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = new ArrayList<SimpleGrantedAuthority>(
                permissions.stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.name()))
                        .toList()
        );

        authorities.add(new SimpleGrantedAuthority("ROLE_" +this.name()));
        return authorities;
    }
}
