package com.single.foodapi.handler;

import com.single.foodapi.client.FoodRequestForm;
import com.single.foodapi.client.edamam.FoodClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class FoodHandler {

    private final FoodClient foodClient;

    @Value("${app.key}")
    private String APP_KEY;

    @Value("${app.id}")
    private String APP_ID;

    public Mono<ServerResponse> getFoodHandler(ServerRequest request) {

        String type = "public";
        String dietType = "balanced";
        String cuisineType = "American";
        String imageSize = "REGULAR";


        FoodRequestForm form = FoodRequestForm.builder()
                .type(type)
                .app_id(APP_ID)
                .app_key(APP_KEY)
                .cuisineType(cuisineType)
                .diet(dietType)
                .imageSize(imageSize)
                .build();

        Mono<ServerResponse> responseMono = foodClient.getFoods(form)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));

        return responseMono;
    }
}
