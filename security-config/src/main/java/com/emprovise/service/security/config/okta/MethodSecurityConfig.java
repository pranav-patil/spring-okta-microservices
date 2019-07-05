package com.emprovise.service.security.config.okta;


import com.emprovise.service.security.config.access.CustomSecurityExpressionHandler;
import com.emprovise.service.security.config.access.PermissionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Bean
    public PermissionHandler permissionEvaluator(){
        return new PermissionHandler();
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CustomSecurityExpressionHandler expressionHandler = new CustomSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator());
        return expressionHandler;
    }
}
