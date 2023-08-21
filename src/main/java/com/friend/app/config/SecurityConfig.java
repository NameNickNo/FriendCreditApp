package com.friend.app.config;

import com.friend.app.models.person.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final String ADMIN_REQUESTS = "/admin/**";
    private final String[] AUTH_REQUESTS = new String[]{"/auth/**", "/error"};
    private final String[] USER_REQUESTS = new String[]{"/api/**", "/message/**", "/debt/**", "/person/**","/friendship/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .httpBasic().and().csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(AUTH_REQUESTS).permitAll();
                    auth.requestMatchers(USER_REQUESTS).hasAnyRole(Role.getUserShortName(), Role.getAdminShortName());
                    auth.requestMatchers(ADMIN_REQUESTS).hasRole(Role.getAdminShortName());
                })
                .formLogin(login -> {
                    login.loginPage("/auth/login");
                    login.defaultSuccessUrl("/person");
                    login.failureUrl("/auth/login?error");
                })
                .logout(logout -> {
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"));
                    logout.logoutSuccessUrl("/auth/login");
                    logout.clearAuthentication(true);
                    logout.invalidateHttpSession(true);
                    logout.deleteCookies("JSESSIONID");
                });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
