package org.kbalazs.smart_scrum_poker_backend_native.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class HttpSecurityConfig
{
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception
    {
        http.authorizeHttpRequests(requests -> requests.requestMatchers(new AntPathRequestMatcher("/ws")).permitAll());
        http.authorizeHttpRequests(requests -> requests.requestMatchers(new AntPathRequestMatcher("/health/**")).permitAll());

        return http.build();
    }
}
