package mr.demonid.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class FileDownloader {

    /**
     * Загрузка с удаленного сервера картинки.
     * @param imageUrl    Полный путь до ресурса.
     * @param accessToken Валидный токен, полученный от сервера аутентификации.
     * @return null - при неудаче.
     */
    public BufferedImage downloadImage(String imageUrl, String accessToken) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(imageUrl))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() == 200) {
                return ImageIO.read(response.body());
            }
            System.out.println("Ошибка загрузки: " + response.statusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Загрузка текстового файла с удаленного сервера.
     * @param fileUrl     Полный путь до ресурса.
     * @param accessToken Валидный токен, полученный от сервера аутентификации.
     * @return null - при неудаче.
     */
    public String downloadTextFile(String fileUrl, String accessToken) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileUrl))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() == 200) {
                try (InputStream inputStream = response.body();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        return reader.lines().collect(Collectors.joining("\n"));
                }
            }
            System.out.println("Ошибка загрузки: " + response.statusCode());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Загрузка ресурса с удаленного сервера в файл.
     * @param fileUrl         Полный путь до ресурса.
     * @param accessToken     Валидный токен, полученный от сервера аутентификации.
     * @param destinationPath Путь и имя для сохранения результата.
     * @return false - если произошла ошибка.
     */
    public boolean downloadFile(String fileUrl, String accessToken, String destinationPath) {

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileUrl))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() == 200) {
                try (InputStream inputStream = response.body();
                     FileOutputStream fileOutputStream = new FileOutputStream(destinationPath)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Файл '" + destinationPath + "' загружен.");
                }
            } else {
                throw new RuntimeException("Ошибка загрузки: " + response.statusCode());
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
