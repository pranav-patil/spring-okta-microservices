package com.emprovise.service.security.config.access;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;


public class PermissionHandler implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object role) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object arg1, Object role) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) principal.getAuthorities();

        if(authorities != null) {
            return authorities.stream().anyMatch(x -> x.getAuthority().equalsIgnoreCase(role.toString()));
        }
        return false;
    }
}
