package com.web.mighigankoreancommunity.error;

public class DuplicateInventoryException extends RuntimeException {
    public DuplicateInventoryException(String message) {
        super(message);
    }

    public DuplicateInventoryException() {
        super("Duplicated Inventory");
    }
}
