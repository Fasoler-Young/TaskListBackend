package com.javabegin.tasklist.backendspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                // TODO сделать создание юзеров также и от лица админа
                    .antMatchers("/registration/user").not().hasRole("USER")
                    .antMatchers("/registration/admin").hasRole("ADMIN")
                    .antMatchers("/registration/updateRole").hasRole("ADMIN")
                    .antMatchers("/registration/deleteByLogin").hasRole("ADMIN")
                    .antMatchers("/registration/all").hasRole("ADMIN")

                .antMatchers("/swagger-ui-custom.html").permitAll()
                .antMatchers("/task/**", "/category/**", "/priority/**",
                        "/stat/**", "/registration/**").authenticated()

                .and().formLogin().defaultSuccessUrl("/swagger-ui-custom.html")
                .and().logout()
                .and().rememberMe();
    }

}
