package com.warp.controllers;

import io.javalin.http.Context;

public class HomeController {
    private static record GreetResponse(
            String message) {
    }

    public void greet(Context ctx) {
        var res = new GreetResponse("welcome to our website");
        ctx.json(res);
    }
}
