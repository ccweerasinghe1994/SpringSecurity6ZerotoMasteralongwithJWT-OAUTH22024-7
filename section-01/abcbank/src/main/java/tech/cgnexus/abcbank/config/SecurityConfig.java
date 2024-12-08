package tech.cgnexus.abcbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
        http.authorizeHttpRequests(
                (requests) -> requests.requestMatchers("my-account", "my-balance", "my-cards", "my-loans").authenticated()
                        .requestMatchers("contact", "notices", "error").permitAll()
        );
        http.formLogin(flc -> flc.disable());
        http.httpBasic(htc -> htc.disable());
        return http.build();
    }
}
