package mr.demonid.client.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@Service
public class FileDownloadService {

    private final RestTemplate restTemplate;

    public FileDownloadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public byte[] downloadFileWithToken(String url, String accessToken) throws Exception {
        // Делаем запрос к серверу и получаем ответ
        ResponseEntity<ByteArrayResource> response = doLoad(url, accessToken);

        // Проверяем статус ответа
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            System.out.println("load " + response.getBody().getByteArray().length + " bytes");
            return response.getBody().getByteArray();
        } else {
            System.out.println("Ошибка загрузки файла");
            throw new Exception("Ошибка загрузки файла");
        }
    }
    public String downloadTextFileWithToken(String url, String accessToken) throws Exception {
        ResponseEntity<ByteArrayResource> response = doLoad(url, accessToken);
        // Проверяем статус ответа
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            System.out.println("load " + response.getBody().getByteArray().length + " bytes");

            InputStream inputStream = response.getBody().getInputStream();
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

        } else {
            System.out.println("Ошибка загрузки файла");
            throw new Exception("Ошибка загрузки файла");
        }
    }

    private ResponseEntity<ByteArrayResource> doLoad(String url, String accessToken) throws Exception {
        // Создаем заголовки с токеном
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);         // Добавляем токен как Bearer в заголовок

        // Формируем запрос
        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, new URI(url));

        // Делаем запрос к серверу и получаем ответ
        return restTemplate.exchange(request, ByteArrayResource.class);
    }


}
