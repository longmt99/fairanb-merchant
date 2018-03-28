package com.fairanb.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceConfiguration extends ResourceServerConfigurerAdapter{
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/webjars/**", "/*.html", "/favicon.ico", "/**/*.css", "/**/*.js", "/v2/api-docs/**", "/swagger-resources/**").permitAll()
                .anyRequest().permitAll();
                //.antMatchers(HttpMethod.GET).permitAll()
                //.antMatchers(HttpMethod.POST, URI.CUSTOMERS + "/**").hasAnyAuthority("ADMIN","ROOT")
                //.antMatchers(HttpMethod.PUT, URI.CUSTOMERS + "/**").hasAnyAuthority("ADMIN","ROOT")
                //.antMatchers(HttpMethod.DELETE, URI.CUSTOMERS + "/**").hasAnyAuthority("ROOT")
                //.anyRequest().authenticated();
        
    }
}
