package ua.everybuy.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ua.everybuy.service.user.UserActivityService;
import ua.everybuy.service.auth.WebClientAuthService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class UserActivityFilter implements GlobalFilter {

    private final WebClientAuthService webClientAuthService;
    private final UserActivityService activityService;

    private final Cache<String, Long> tokenCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    private final Cache<Long, Boolean> activityCache = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        Long cachedUserId = tokenCache.getIfPresent(token);
        if (cachedUserId != null) {
            return updateActivityIfNeeded(cachedUserId)
                    .then(chain.filter(exchange));
        }

        return Mono.defer(() -> webClientAuthService.validateToken(token))
                .flatMap(authResponse -> {
                    if (authResponse == null || authResponse.getData() == null || !authResponse.getData().getIsValid()) {
                        return chain.filter(exchange);
                    }

                    Long userId = authResponse.getData().getUserId();
                    if (userId != null) {
                        tokenCache.put(token, userId);
                        return updateActivityIfNeeded(userId)
                                .then(chain.filter(exchange));
                    }

                    return chain.filter(exchange);
                });
    }

    private Mono<Void> updateActivityIfNeeded(Long userId) {
        if (activityCache.getIfPresent(userId) == null) {
            activityCache.put(userId, true);
            return activityService.updateLastActive(userId);
        }
        return Mono.empty();
    }
}
