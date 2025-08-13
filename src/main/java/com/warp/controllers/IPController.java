package com.warp.controllers;

import com.warp.services.IPService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class IPController implements Controller {
    private final IPService ipService;
    private final int port;

    public IPController(IPService ipService, int port) {
        this.ipService = ipService;
        this.port = port;
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.get("/api/local-ip", this::getIP);
    }

    private static record IPResponse(String ip) {
    }

    public void getIP(Context ctx) throws Exception {
        var ip = ipService.getNetworkIp();
        String suffix = String.format(":%d", port);
        ctx.json(new IPResponse(ip.toString() + suffix));
    }
}
