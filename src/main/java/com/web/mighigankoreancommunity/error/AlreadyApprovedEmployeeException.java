package com.web.mighigankoreancommunity.error;

public class AlreadyApprovedEmployeeException extends RuntimeException {
    public AlreadyApprovedEmployeeException(String message) {
        super(message);
    }

    public AlreadyApprovedEmployeeException() {
        super("Already Approved");
    }
}
