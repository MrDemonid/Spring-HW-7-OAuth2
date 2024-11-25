package mr.demonid.resource.server.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ResourceController {

    /**
     * Передача файла авторизированному клиенту.
     * @param filename имя файла.
     * @return Если файл существует, то будет передан в теле ответа, как вложение.
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename) {
        System.out.println("Download " + filename);
        try {
            Resource resource = new ClassPathResource("static/" + filename);

            if (resource.exists() || resource.isReadable()) {       // файл существует и доступен?

                // Настраиваем заголовки для корректного ответа
                HttpHeaders headers = new HttpHeaders();
                // "Content-Disposition" - файл должен быть загружен как вложение
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
                return ResponseEntity.ok().headers(headers).body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
