package mr.demonid.client.controller;

import lombok.AllArgsConstructor;
import mr.demonid.client.service.FileDownloadService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;

@Controller
@AllArgsConstructor
public class WebClientController {

    FileDownloadService downloadService;

    @GetMapping
    public String getFile(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User,
            Model model)
    {
        System.out.println("do getFile()....");
        try {
            String url = "http://localhost:8070/download";
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            byte[] image = downloadService.downloadFileWithToken(url+"?filename=pk8000.png", accessToken);
            String text = downloadService.downloadTextFileWithToken(url+"?filename=read.me", accessToken);
            model.addAttribute("imageBase64", Base64.getEncoder().encodeToString(image));
            model.addAttribute("fileContent", text);
            return "/home-page";
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            model.addAttribute("errorMessage", "ОШИБКА!");
            model.addAttribute("errorDetails", e.getMessage());
            return "/error";
        }
    }
}
