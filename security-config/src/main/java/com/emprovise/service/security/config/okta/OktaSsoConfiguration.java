package com.emprovise.service.security.config.okta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoDefaultConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.RequestEnhancer;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@EnableOAuth2Sso
@EnableWebSecurity
@Configuration
@ConditionalOnProperty(prefix = "spring.security", name = "enabled", matchIfMissing = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true, jsr250Enabled = true)
public class OktaSsoConfiguration extends OAuth2SsoDefaultConfiguration {

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;
    @Autowired
    private AuthorizationCodeResourceDetails details;
    @Autowired
    private Environment env;
    @Autowired
    private OKTALogoutHandler oktaLogoutHandler;

    public OktaSsoConfiguration(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver simpleMappingExceptionResolverObject = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty(".RuntimeException", "/ErrorPage");
        simpleMappingExceptionResolverObject.setDefaultErrorView("uncaughtException");
        simpleMappingExceptionResolverObject.setOrder(1);
        simpleMappingExceptionResolverObject.setExceptionMappings(properties);
        return simpleMappingExceptionResolverObject;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/servlet/DateServlet", "/version", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        RequestMatcher logoutRequestMatcher = new AntPathRequestMatcher("/custom/logout");
        super.configure(http );
        http.headers().frameOptions().disable();
        http.cors().disable();
        http.logout().invalidateHttpSession(true)
            .clearAuthentication(true).deleteCookies("JSESSIONID").deleteCookies("SESSION").
            logoutRequestMatcher(logoutRequestMatcher).addLogoutHandler(oktaLogoutHandler);
        http.csrf().disable();
    }

    @Bean
    public OAuth2RestTemplate userInfoRestTemplate() {
        OAuth2RestTemplate userInfoRestTemplate = new OAuth2RestTemplate(details, oauth2ClientContext);
        userInfoRestTemplate.getInterceptors().add(new AcceptJsonRequestInterceptor());
        AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider();
        accessTokenProvider.setTokenRequestEnhancer(new AcceptJsonRequestEnhancer());
        userInfoRestTemplate.setAccessTokenProvider(accessTokenProvider);
        return userInfoRestTemplate;
    }

    static class AcceptJsonRequestInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            return execution.execute(request, body);
        }
    }

    static class AcceptJsonRequestEnhancer implements RequestEnhancer {

        @Override
        public void enhance(AccessTokenRequest request,
                            OAuth2ProtectedResourceDetails resource,
                            MultiValueMap<String, String> form, HttpHeaders headers) {
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        }
    }
}
