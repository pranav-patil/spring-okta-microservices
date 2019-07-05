
package com.emprovise.service.security.config.okta;


import com.emprovise.service.security.service.AuthorizationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.DelegatingJwtClaimsSetVerifier;
import org.springframework.security.oauth2.provider.token.store.IssuerClaimVerifier;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

@Configuration
@EnableResourceServer
@Order(102)
public class SpringWebSecurityConfig extends ResourceServerConfigurerAdapter {

    @Value("${security.oauth2.resource.jwk.key-set-uri}")
    private String keySetUri;

    @Value("${security.oauth2.resource.claim}")
    private String issClaim;

    @Autowired
    private AuthorizationUserDetailsService authorizationUserDetailsService;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(null);
    }

    @Bean
    public TokenStore jwkTokenStore() {
        return new JwkTokenStore(keySetUri, accessTokenConverter(), jwtClaimsSetVerifier());
    }

    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setJwtClaimsSetVerifier(jwtClaimsSetVerifier());
        ConfigurableAccessTokenConverter configurableAccessTokenConverter = new ConfigurableAccessTokenConverter("scp", "groups");
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(authorizationUserDetailsService);
        configurableAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        converter.setAccessTokenConverter(configurableAccessTokenConverter);
        return converter;
    }

    @Bean
    public JwtClaimsSetVerifier issuerClaimVerifier() {
        try {
            return new IssuerClaimVerifier(new URL(issClaim));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jwkTokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public JwtClaimsSetVerifier customJwtClaimVerifier() {
        return new CustomClaimVerifier();
    }

    @Bean
    public JwtClaimsSetVerifier jwtClaimsSetVerifier() {
        return new DelegatingJwtClaimsSetVerifier(
            Arrays.asList(issuerClaimVerifier(), customJwtClaimVerifier()));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new OAuthRequestedMatcher())
            .authorizeRequests().anyRequest().fullyAuthenticated();
    }
}
