package mr.demonid.auth.server.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * В этом классе будем хранить подгружаемые из application.yml настройки.
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.authorization-server")
public class AuthorizationProperties {
    // адрес сервера авторизации
    private String issuerUrl;
    // id и секрет web-клиента, а так же его адреса для перехода в случае успешной авторизации
    private String clientId;
    private String clientSecret;
    private List<String> clientUrls;
    // id и секрет программы без участия пользователя
    private String apmId;
    private String apmSecret;
}