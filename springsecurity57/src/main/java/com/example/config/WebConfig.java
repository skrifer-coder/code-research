package com.example.config;

import com.example.filter.LoginFilter;
import com.example.handlers.MyAuthenticationFailureHandler;
import com.example.handlers.MyAuthenticationSuccessHandler;
import com.example.handlers.MyLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.annotation.Resource;

@Configuration
public class WebConfig {

    @Resource
    UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //注入过滤器
    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager) {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setPasswordParameter("password");
        loginFilter.setUsernameParameter("username");
        loginFilter.setFilterProcessesUrl("/login");
        loginFilter.setAuthenticationManager(authenticationManager);
//        loginFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
//        loginFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        return loginFilter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and().formLogin()
                .successHandler(new MyAuthenticationSuccessHandler()).failureHandler(new MyAuthenticationFailureHandler())
                //通过 logout() 方法开启注销配置
                .and().logout().logoutSuccessHandler(new MyLogoutSuccessHandler())
                /**
                 * logoutUrl 指定退出登录请求地址，默认是 GET 请求，路径为 /logout
                 * invalidateHttpSession 退出时是否使 session 失效，默认值为 true
                 * clearAuthentication 退出时是否清除认证信息，默认值为 true
                 * 可以配置多个注销登录的请求，指定方法
                 */
                .logoutRequestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/logout", "GET"), new AntPathRequestMatcher("/logout1", "GET")))
                .and().userDetailsService(userDetailsService)
                .csrf().disable();
        httpSecurity.addFilterAt(loginFilter(httpSecurity.getSharedObject(AuthenticationManager.class)), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
