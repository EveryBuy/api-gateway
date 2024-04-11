package ua.everybuy.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import ua.everybuy.errorhandling.ErrorResponse;
import ua.everybuy.errorhandling.MessageResponse;

@Component
@RequiredArgsConstructor
public class GlobalExceptionFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpStatusCode statusCode = exchange.getResponse().getStatusCode();

            if (statusCode != null && statusCode.is5xxServerError()) {
                ServerHttpResponse response = exchange.getResponse();
                ErrorResponse responseBody = new ErrorResponse(statusCode.value(), new MessageResponse("SERVER_ERROR"));
                byte[] responseBytes;
                try {
                    responseBytes = objectMapper.writeValueAsBytes(responseBody);
                } catch (JsonProcessingException e) {
                    responseBytes = "{}" .getBytes();
                }
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                response.getHeaders().setContentLength(responseBytes.length);

                response.writeWith(Mono.just(response.bufferFactory().wrap(responseBytes))).subscribe();
            }
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
