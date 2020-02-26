package com.karpovich.homework.api;

public enum ResponseCode {
    SUCCESS(200),
    INVALID_REQUEST(400),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    INTERNAL_SERVER_ERROR(500),
    DEFAULT(0);

    private int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
