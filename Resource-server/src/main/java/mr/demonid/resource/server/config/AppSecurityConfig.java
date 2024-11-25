package mr.demonid.resource.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация сервера ресурсов.
 */
@Configuration
public class AppSecurityConfig {
    /**
     * Настраиваем аутентификацию.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(e -> e
                .anyRequest().authenticated())              // аутентификация для любых запросов
                .oauth2ResourceServer(t -> t                // используем jwt-токен
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

}
