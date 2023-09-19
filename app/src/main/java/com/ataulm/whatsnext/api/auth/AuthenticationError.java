package com.ataulm.whatsnext.api.auth;

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

        EXCHANGING_REFRESH_TOKEN_FOR_FRESH_TOKEN,
        EXCHANGING_CREDENTIALS_FOR_TOKEN
    }
}
