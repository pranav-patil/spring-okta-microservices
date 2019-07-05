package com.emprovise.service.security.config.okta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Component
public class OKTALogoutHandler extends SecurityContextLogoutHandler {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private Environment env;
//    @Value("HOST")
    private String hostName;

    @Override
    public void logout(HttpServletRequest httServletRequest, HttpServletResponse httpServletResponse,
                       Authentication authentication) {
        StringBuilder redirectURLBuilder = new StringBuilder();
        OAuth2RestTemplate restTemplate = this.context.getBean(UserInfoRestTemplateFactory.class).getUserInfoRestTemplate();
        String oktaLogoutURL = env.getProperty("security.oauth2.sso.oktaLogoutUri");
        OAuth2AccessToken accessToken = restTemplate.getOAuth2ClientContext().getAccessToken();
        String idToken = null;
        if ((accessToken != null) && (accessToken.getAdditionalInformation() != null)
            && (accessToken.getAdditionalInformation().containsKey("id_token"))) {
            idToken = accessToken.getAdditionalInformation().get("id_token").toString();
        }

        if (idToken != null) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(oktaLogoutURL).queryParam("id_token_hint",
                idToken).queryParam("post_logout_redirect_uri", env.getProperty("security.oauth2.sso.logout-redirecturi")+hostName+"/");
            redirectURLBuilder.append(builder.toUriString());
        } else {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(env.getProperty("security.oauth2.sso.logout-redirecturi")+hostName+"/");
            redirectURLBuilder.append(builder.toUriString());
        }

        try {
            removeAllSessionAttributes(httServletRequest);
            removeAllCookies(httServletRequest);
            super.logout(httServletRequest, httpServletResponse, authentication);
            httServletRequest.logout();
            httpServletResponse.sendRedirect(redirectURLBuilder.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


    private void removeAllSessionAttributes(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession();

            if (session != null) {
                Enumeration<String> attributes = session.getAttributeNames();

                if (attributes != null) {
                    while (attributes.hasMoreElements()) {
                        session.removeAttribute(attributes.nextElement());
                    }
                }
            }
        }
    }

    private void removeAllCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(null);
            }
        }
    }
}
