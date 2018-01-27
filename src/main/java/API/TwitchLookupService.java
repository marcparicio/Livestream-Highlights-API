package API;

import API.Model.Clip;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.CompletableFuture;

@Service
public class TwitchLookupService {

    private final RestTemplate restTemplate;

    public TwitchLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();

    }

    @Async
    public CompletableFuture<Clip> getClip(String slug) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Accept","application/vnd.twitchtv.v5+json");
        httpHeaders.set("Client-id",ApiUtils.clientId);
        HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);

        String url = String.format("https://api.twitch.tv/kraken/clips/{slug}");
        ResponseEntity<String> resultClip1 = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, slug);
        System.out.print(resultClip1.getStatusCode().toString() + "\n");
        System.out.print(resultClip1.getBody() + "\n");

        ResponseEntity<Clip> resultClip = restTemplate.exchange(url, HttpMethod.GET, entity, Clip.class, slug);
        return CompletableFuture.completedFuture(resultClip.getBody());
    }
}
