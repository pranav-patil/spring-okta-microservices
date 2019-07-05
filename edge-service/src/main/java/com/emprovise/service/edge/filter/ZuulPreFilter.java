package com.emprovise.service.edge.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ZuulPreFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        if(SecurityContextHolder.getContext() != null &&
           SecurityContextHolder.getContext().getAuthentication() != null &&
           SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {

            Authentication oAuth2Authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = getPrincipalUserFromSecurityContext(oAuth2Authentication);
            String accessToken = getAccessToken(oAuth2Authentication);
            ctx.addZuulRequestHeader("Authorization", "Bearer " + accessToken);
            logger.info(String.format("UserId is %s and Access Token is %s", userId, accessToken));
        }

        logger.info("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
        return null;
    }

    private String getAccessToken(Authentication oAuth2Authentication) {
        if (oAuth2Authentication instanceof OAuth2Authentication) {
            OAuth2Authentication userObject = (OAuth2Authentication) oAuth2Authentication;
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) userObject.getDetails();
            if (details != null) {
                return details.getTokenValue();
            }
        }
        return null;
    }

    private String getPrincipalUserFromSecurityContext(Authentication authentication) {
        String userID;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken userObject = (UsernamePasswordAuthenticationToken) authentication;
            org.springframework.security.core.userdetails.User principalObject = (org.springframework.security.core.userdetails.User) userObject.getPrincipal();
            userID = principalObject.getUsername();
        } else {
            OAuth2Authentication userObject = (OAuth2Authentication) authentication;
            org.springframework.security.core.userdetails.User principalObject = (org.springframework.security.core.userdetails.User) userObject.getPrincipal();
            userID = principalObject.getUsername();
        }
        return userID;
    }
}
