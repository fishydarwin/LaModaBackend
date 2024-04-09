package com.github.fishydarwin.LaModaBackend.util.imgur;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Fetched from my previous project: Kittycats-Java
 * https://github.com/fishydarwin/Kittycats-Java/blob/master/src/main/java/me/darwj/kittycats/imgur/ImgurBrowser.java
 */
public class ImgurBrowser {

    private static String previousQuery = "";
    private static final List<URL> cachedImageURLs = new ArrayList<>();

    public static void grabImgurImages(String query)
            throws URISyntaxException, IOException, InterruptedException, ParseException {

        cachedImageURLs.clear();
        previousQuery = query;

        Random random = new Random();
        int rPage = random.nextInt(1, 20);

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(new URI("https://api.imgur.com/3/gallery/search/time/all/" + rPage + "?q=" + query))
                        .GET()
                        .headers("Authorization", "Client-ID 97782a37bb10062")
                        .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(response.body());

        JSONArray dataArray = (JSONArray) json.get("data");

        for (Object o : dataArray) {
            try {
                JSONObject dataObject = (JSONObject) o;
                JSONArray imageArray = (JSONArray) dataObject.get("images");
                for (Object value : imageArray) {
                    JSONObject imageObject = (JSONObject) value;
                    String link = (String) imageObject.get("link");
                    if (link.endsWith("png") || link.endsWith("jpg")) {
                        cachedImageURLs.add(new URL(link));
                    }
                }
            } catch (Exception ex) { /* just skip */ }
        }

    }

    public static BufferedImage getRandomImage()
            throws URISyntaxException, IOException, ParseException, InterruptedException {

        if (cachedImageURLs.isEmpty()) grabImgurImages(previousQuery);

        Random random = new Random();
        int randomIndex = random.nextInt(0, cachedImageURLs.size());

        URL imageURL = cachedImageURLs.get(randomIndex);
        cachedImageURLs.remove(randomIndex);

        HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
        connection.connect();
        BufferedImage image = ImageIO.read(connection.getInputStream());
        connection.disconnect();

        return image;
    }

    public static URL getRandomImageURL()
            throws URISyntaxException, IOException, ParseException, InterruptedException {

        if (cachedImageURLs.isEmpty()) grabImgurImages(previousQuery);

        Random random = new Random();
        int randomIndex = random.nextInt(0, cachedImageURLs.size());

        URL imageURL = cachedImageURLs.get(randomIndex);
        cachedImageURLs.remove(randomIndex);

        return imageURL;
    }

}
