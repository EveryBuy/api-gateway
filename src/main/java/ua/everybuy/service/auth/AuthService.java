package ua.everybuy.service.auth;

import reactor.core.publisher.Mono;
import ua.everybuy.dto.UserAuthResponse;

public interface AuthService {
    Mono<UserAuthResponse> validateToken(String token);
}
