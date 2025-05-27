package tech.buildrun.springsecurity.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

@Service
public class PasswordValidationService {

    public boolean isPasswordPwned(String prefix, String suffix) {
        HttpClient client = HttpClient.newHttpClient();
        String apiUrl = "https://api.pwnedpasswords.com/range/" + prefix;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<Stream<String>> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofLines()
            );
            // Verifica se algum sufixo retornado pela API corresponde ao sufixo da senha
            return response.body().anyMatch(line -> line.startsWith(suffix));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}