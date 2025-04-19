package com.web.mighigankoreancommunity.error;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String message) {
        super(message);
    }

    public InventoryNotFoundException() {
        super("Inventory Not Found");
    }
}
