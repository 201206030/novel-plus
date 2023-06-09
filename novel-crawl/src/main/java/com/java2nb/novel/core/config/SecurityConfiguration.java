package com.java2nb.novel.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity配置
 *
 * @author Administrator
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${admin.username}")
    private String username;

    @Value("${admin.password}")
    private String password;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        User.UserBuilder builder = User.builder().passwordEncoder(passwordEncoder()::encode);
        auth.inMemoryAuthentication().withUser(builder.username(username).password(password).roles("ADMIN").build());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/**").hasRole("ADMIN")
            .and().formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll()
            .and().httpBasic();

    }


}
