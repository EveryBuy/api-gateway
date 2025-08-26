package ua.everybuy.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebClientActivityService implements UserActivityService {

    private static final String SERVICE_PASSWORD_HEADER = "Service-Password";
    private static final String SERVICE_PASSWORD = "123";
    private static final String UPDATE_ACTIVITY_URI_TEMPLATE = "/user/{userId}/activity";
    private final WebClient webClient;

    public WebClientActivityService(WebClient.Builder webClientBuilder,
                                    @Value("${user.service.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<Void> updateLastActive(Long userId) {
        return webClient.patch()
                .uri(UPDATE_ACTIVITY_URI_TEMPLATE, userId)
                .header(SERVICE_PASSWORD_HEADER, SERVICE_PASSWORD)
                .exchangeToMono(response -> {
                    log.info("Request to update user {} activity, status: {}", userId, response.statusCode());
                    return response.bodyToMono(String.class)
                            .doOnNext(body -> log.info("Response body: {}", body))
                            .then();
                })
                .doOnError(e -> log.error("Error updating user {} activity: {}", userId, e.getMessage()))
                .onErrorResume(e -> Mono.empty());
    }
}
