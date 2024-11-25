package mr.demonid.auth.server.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;

import java.util.UUID;

import static org.springframework.security.oauth2.core.AuthorizationGrantType.*;
import static org.springframework.security.oauth2.core.ClientAuthenticationMethod.CLIENT_SECRET_BASIC;


/**
 * Конфигурация сервера авторизации
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthorizationProperties authorizationProperties;

    /**
     * Включаем в цепочку security свои AuthenticationProvider + UserDetailService + PasswordEncoder
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, CustomAuthenticationProvider authenticationProvider) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * Репозиторий для хранения зарегистрированных клиентов.
     *
     * @param jdbcTemplate объект подключения к БД.
     * @return репозиторий.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * Регистрируем (добавляем в БД) клиента по умолчанию.
     */
    @Bean
    ApplicationRunner clientRunner(RegisteredClientRepository registeredClientRepository, CustomPasswordEncoder passwordEncoder) {
        return args -> {
            // Создание веб-клиента
            if (registeredClientRepository.findByClientId(authorizationProperties.getClientId()) == null) {
                registeredClientRepository.save(RegisteredClient
                        .withId(UUID.randomUUID().toString())
                        .clientId(authorizationProperties.getClientId())
                        .clientSecret(passwordEncoder.encode(authorizationProperties.getClientSecret()))
                        .authorizationGrantType(AUTHORIZATION_CODE)
                        .authorizationGrantType(CLIENT_CREDENTIALS)
                        .authorizationGrantType(REFRESH_TOKEN)
                        .redirectUris(u -> u.addAll(authorizationProperties.getClientUrls()))
                        .scope("user.read")
                        .scope("user.write")
                        .scope("openid")
                        .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(true)      // запрос пользователя разрешения на "read", "write" и тд.
                                .build())
                        .build()
                );
            }
            // Создание клиента для программы, работающей без участия пользователя
            if (registeredClientRepository.findByClientId(authorizationProperties.getApmId()) == null) {
                registeredClientRepository.save(RegisteredClient
                        .withId(UUID.randomUUID().toString())
                        .clientId(authorizationProperties.getApmId())
                        .clientSecret(passwordEncoder.encode(authorizationProperties.getApmSecret()))
                        .authorizationGrantType(CLIENT_CREDENTIALS)
                        .scope("user.read")
                        .scope("user.write")
                        .clientAuthenticationMethod(CLIENT_SECRET_BASIC)
                        .build()
                );
            }
        };
    }


    /**
     * Зададим корневой URL нашего сервера.
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(authorizationProperties.getIssuerUrl())
                .build();
    }


}