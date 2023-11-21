package com.acosux.MSBiosuper.config;

import com.acosux.MSBiosuper.security.CorsFilter;
import com.acosux.MSBiosuper.security.VerifyTokenFilter;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
        // -- swagger ui
        "/v2/api-docs",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().and()
                .anonymous().and()
                .csrf().disable()
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                .addFilterBefore(new VerifyTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                //                .addFilterBefore(new GenerateTokenForUserFilter("/session", authenticationManager(), tokenUtil), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().
                antMatchers(AUTH_WHITELIST).permitAll(). // whitelist URL permitted
                antMatchers("/**")
                .authenticated().and().sessionManagement().maximumSessions(1);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/",
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

}
