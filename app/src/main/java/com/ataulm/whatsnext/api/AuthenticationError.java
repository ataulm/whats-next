package com.ataulm.whatsnext.api;

public class AuthenticationError extends RuntimeException {

    private final Type type;

    public AuthenticationError(Type type) {
        super();
        this.type = type;
    }

    public AuthenticationError(Throwable throwable, Type type) {
        super(throwable);
        this.type = type;
    }

    public enum Type {

        REQUIRES_USER_SIGN_IN,
        EXCHANGING_REFRESH_TOKEN_FOR_FRESH_TOKEN,
        EXCHANGING_CREDENTIALS_FOR_TOKEN
    }
}
