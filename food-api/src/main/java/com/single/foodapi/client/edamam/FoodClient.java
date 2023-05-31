package com.single.foodapi.client.edamam;

import com.single.foodapi.client.FoodRequestForm;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;


@ReactiveFeignClient(
        name = "edamam",
        url = "${edamam.urls.base-url}")
public interface FoodClient {
    @GetMapping
    Mono<String> getFoods(@SpringQueryMap FoodRequestForm form);
}
