package mr.demonid.controller;

import mr.demonid.model.FileDownloader;
import mr.demonid.model.OAuth2Client;
import mr.demonid.view.IView;

import java.awt.image.BufferedImage;

public class Controller {

    private final static String CLIENT_ID = "apmid";
    private final static String CLIENT_SECRET = "hren-ugadaesh";
    private final static String AUTH_URL = "http://localhost:8090/oauth2/token";

    private final static String TEXT_FILE = "http://localhost:8070/download?filename=read.me";
    private final static String IMAGE_FILE = "http://localhost:8070/download?filename=pk8000.png";

    IView view;

    public Controller(IView view) {
        this.view = view;
        init();
    }


    private void init() {
        OAuth2Client client = new OAuth2Client(CLIENT_ID, CLIENT_SECRET, AUTH_URL);
        String accessToken = client.getAccessToken();
        view.appendLog("Access token: " + (accessToken.isBlank() ? "Resource server not found!" : accessToken));
        if (!accessToken.isEmpty()) {
            FileDownloader fileDownloader = new FileDownloader();
            view.appendLog("load text file...");
            String text = fileDownloader.downloadTextFile(TEXT_FILE, accessToken);
            if (text != null && !text.isBlank()) {
                view.setText(text);
            } else {
                view.appendLog("  -- error: resource file not found!");
            }
            view.appendLog("load image file...");
            BufferedImage image = fileDownloader.downloadImage(IMAGE_FILE, accessToken);
            if (image != null) {
                view.setImage(image);
            } else {
                view.appendLog("  -- error: image not found!");
            }
            view.appendLog("program run ok.");
        }

    }
}
