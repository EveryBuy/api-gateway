package ua.everybuy.service.user;

import reactor.core.publisher.Mono;

public interface UserActivityService {
    Mono<Void> updateLastActive(Long userId);
}