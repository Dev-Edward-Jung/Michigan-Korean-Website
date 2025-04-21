package com.web.mighigankoreancommunity.error;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(String message) {
        super(message);
    }

  public InvitationNotFoundException() {
    super("Invitation not found.");
  }
}
