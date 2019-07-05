package com.emprovise.service.security.config.okta;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;


public class OAuthRequestedMatcher implements RequestMatcher {

    public boolean matches(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        return (auth != null && auth.startsWith("Bearer")) || (request.getParameter("access_token") != null);
    }
}
