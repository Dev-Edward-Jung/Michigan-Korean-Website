package com.web.mighigankoreancommunity.error;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException() {
        super("Restaurant not found.");
    }
}
