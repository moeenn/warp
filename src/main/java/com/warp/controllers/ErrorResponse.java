package com.warp.controllers;

public record ErrorResponse(String error) {
    public static ErrorResponse fromException(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
