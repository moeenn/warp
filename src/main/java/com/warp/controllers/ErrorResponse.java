package com.warp.controllers;

public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public static ErrorResponse fromError(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

    public String getError() {
        return error;
    }
}
