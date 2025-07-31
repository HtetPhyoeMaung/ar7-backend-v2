package com.security.spring.exceptionall;

public enum ErrorCodes {
    NOT_FOUND(404), BAD_REQUEST(400), UNAUTHORIZED(401), FORBIDDEN(403), CONFLICT(409), INTERNAL_SERVER_ERROR(500);

    public final int code;

    ErrorCodes(int code) {
        this.code = code;
    }
}
