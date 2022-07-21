package com.springboot.provider.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @deprecated Use a {@link WebSecurityConfig} Bean to configure
 */
@Deprecated
//@Configuration
//@EnableWebSecurity
public class WebSecurityConfigDeprecated extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Override this method to configure {@link WebSecurity}. For example, if you wish to
     * ignore certain requests.
     *
     * Endpoints specified in this method will be ignored by Spring Security, meaning it
     * will not protect them from CSRF, XSS, Clickjacking, and so on.
     *
     * Instead, if you want to protect endpoints against common vulnerabilities, then see
     * {@link #configure(HttpSecurity)} and the {@link HttpSecurity#authorizeRequests}
     * configuration method.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                    .frameOptions()
                    // .disable(); // 任何跨域
                    .sameOrigin(); // 同源跨域

        http
                    .cors()
                .and()
                    .csrf().disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                    .antMatchers("/", "/websocket/**", "/api/**", "/test/**", "/his/**", "/lis/**").permitAll()
                    .antMatchers("/user/**").hasRole("USER")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/file/**").hasAnyRole("USER","ADMIN")
                    .anyRequest().authenticated();
//                .and()
//                    .formLogin()
////                    .loginPage("/page/login.ftlh")
//                    .successForwardUrl("/index.ftlh")
//                    .permitAll();
//                .and()
//                    .logout()
//                    .deleteCookies()
//                    .invalidateHttpSession(true)
//                    .clearAuthentication(true)
//                    .permitAll();
    }

    /**
     * Override this method to configure {@link WebSecurity}. For example, if you wish to
     * ignore certain requests.
     *
     * Endpoints specified in this method will be ignored by Spring Security, meaning it
     * will not protect them from CSRF, XSS, Clickjacking, and so on.
     *
     * Instead, if you want to protect endpoints against common vulnerabilities, then see
     * {@link #configure(HttpSecurity)} and the {@link HttpSecurity#authorizeRequests}
     * configuration method.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    /**
     * Allows modifying and accessing the {@link UserDetailsService} from
     * {@link #userDetailsServiceBean()} without interacting with the
     * {@link ApplicationContext}. Developers should override this method when changing
     * the instance of {@link #userDetailsServiceBean()}.
     * @return the {@link UserDetailsService} to use
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        UserDetails user =
                User.withUsername("user")
                        .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123"))
                        .roles("ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    /*
    * 多个继承关系用 \n 隔开即可，如下 ROLE_A > ROLE_B \n ROLE_C > ROLE_D。
    * 如果角色层级关系是连续的，也可以这样配置 ROLE_A > ROLE_B > ROLE_C > ROLE_D。
    * */
    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return hierarchy;
    }
}
