package com.emprovise.service.security.config.access;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }

    void setThis(Object target) {
        this.target = target;
    }

    public boolean canViewStocks() {
        return hasAccess("READ_STOCKS");
    }

    public boolean canViewWeather() {
        return hasAccess("READ_WEATHER");
    }

    public boolean hasAccess(String role) {
        return getLoggedInUserBusinessActivities().stream().anyMatch(x -> x.equalsIgnoreCase(role));
    }

    public List<String> getLoggedInUserBusinessActivities() {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            if(authorities != null) {
                return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
            }
        }

        return new ArrayList<>();
    }
}
