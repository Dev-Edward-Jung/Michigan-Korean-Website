package com.web.mighigankoreancommunity.error;

public class RestaurantEmployeeNotFoundException extends RuntimeException {
    public RestaurantEmployeeNotFoundException(String message) {
        super(message);
    }

    public RestaurantEmployeeNotFoundException() {
      super("Restaurant Employee Not Found");
    }
}
