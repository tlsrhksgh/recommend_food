package com.single.foodapi.router;

import com.single.foodapi.handler.FoodHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class FoodRouter {

    @Bean
    public RouterFunction getFoodRouter(FoodHandler foodHandler) {
        RouterFunction<ServerResponse> foodRoutes = RouterFunctions
                .route(GET("").and(accept(MediaType.APPLICATION_JSON)), foodHandler::getFoodHandler)
                .andRoute(POST(""), foodHandler::getFoodHandler);

        return RouterFunctions.nest(path("/api/v1/foods"), foodRoutes);
    }
}
