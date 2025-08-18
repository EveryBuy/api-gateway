package ua.everybuy.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.everybuy.dto.UserAuthResponse;

@Service
public class WebClientAuthService implements AuthService {
    private static final String AUTH_VALIDATE_URI = "/auth/validate";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final WebClient webClient;

    public WebClientAuthService(WebClient.Builder webClientBuilder,
                                @Value("${auth.service.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<UserAuthResponse> validateToken(String token) {
        return webClient.get()
                .uri(AUTH_VALIDATE_URI)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .retrieve()
                .bodyToMono(UserAuthResponse.class)
                .onErrorResume(e -> Mono.empty());
    }
}

