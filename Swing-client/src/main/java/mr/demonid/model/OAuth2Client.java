package mr.demonid.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

/**
 * Класс для получения токена от сервера аутентификации OAuth2.
 */
public class OAuth2Client {

    private final String clientId;
    private final String clientSecret;
    private final String tokenUrl;

    public OAuth2Client(String clientId, String clientSecret, String tokenUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUrl = tokenUrl;
    }

    public String getAccessToken() {

        try (HttpClient client = HttpClient.newHttpClient()) {
            // кодируем client_id и client_secret в Base64
            String authorizationBase64 = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

            // составляем запрос
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Authorization", "Basic " + authorizationBase64)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return new com.fasterxml.jackson.databind.ObjectMapper()  // парсим JSON-ответ для получения токена
                        .readTree(response.body())
                        .get("access_token")
                        .asText();
            } else {
                throw new RuntimeException("Ошибка получения токена! Код: " + response.statusCode() + ", тело: " + response.body());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }


}
