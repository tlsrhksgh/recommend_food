package com.single.foodapi.client;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodRequestForm {
    private String type;
    private String app_id;
    private String app_key;
    private String diet;
    private String cuisineType;
    private String imageSize;
}
