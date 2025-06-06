package com.web.mighigankoreancommunity.error;

public class UnauthorizedRestaurantAccessException extends RuntimeException {
    public UnauthorizedRestaurantAccessException(String message) {
        super(message);
    }

    public UnauthorizedRestaurantAccessException() {
        super("Unauthorized Restaurant Access");
    }
}
