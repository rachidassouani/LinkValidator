package io.rachidassouani;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static HttpClient client;

    public static void main(String[] args) throws IOException {
        client = HttpClient.newHttpClient();
        Files.lines(Paths.get("urls.txt"))
                .map(Main::validateLink)
                .forEach(System.out::println);

    }

    public static String validateLink(String link) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(link))
                .GET() // GET Method
                .build();
        try {
            HttpResponse<Void> response =
                    client.send(request, HttpResponse.BodyHandlers.discarding());
            return responseToString(response);
        } catch (IOException | InterruptedException e) {
            return String.format("%s -> %s", link, false);
        }
    }

    public static String responseToString(HttpResponse<Void> response) {
        int status = response.statusCode();
        boolean success = status >= 200 && status <= 299;
        return String.format("%s -> %s (status : %s)", response.uri(), status, success);
    }
}
