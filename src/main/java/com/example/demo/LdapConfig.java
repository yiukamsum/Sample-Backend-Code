package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import lombok.ToString;

@Configuration
@ToString
public class LdapConfig {

    @Value("${ldap.url}")
    private String url;
    @Value("${ldap.base}")
    private String base;
    @Value("${ldap.username}")
    private String username;
    @Value("${ldap.password}")
    private String password;
    @Value("${ldap.referral}")
    private String referral;

    @Bean
    public LdapTemplate ldapTemplate(){
        return new LdapTemplate(contextSourceTarget());
    }

    @Bean
    public LdapContextSource contextSourceTarget(){
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(url);
        ldapContextSource.setBase(base);
        ldapContextSource.setUserDn(username);
        ldapContextSource.setPassword(password);
        ldapContextSource.setReferral(referral);
        final Map<String, Object> envProps = new HashMap<>();
        envProps.put("java.naming.ldap.attributes.binary","objectGUID");
        ldapContextSource.setBaseEnvironmentProperties(envProps);
        return ldapContextSource;
    }
}
