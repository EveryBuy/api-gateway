package ua.everybuy.routing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class WakeUpConfig {
    @Bean
    public RouterFunction<ServerResponse> keepAlive() {
        return route(GET("/keep-alive"),
                request -> ok().contentType(MediaType.TEXT_PLAIN).bodyValue("I am alive!"))
                .andRoute(POST("/keep-alive"),
                        request -> request.bodyToMono(String.class)
                                .flatMap(body -> ok().contentType(MediaType.TEXT_PLAIN).bodyValue("Received: " + body)));
    }
}
