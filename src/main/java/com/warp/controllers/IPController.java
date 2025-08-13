package com.warp.controllers;

import com.warp.LocalIP;
import io.javalin.http.Context;

public class IPController {
    private final int port;

    public IPController(int port) {
        this.port = port;
    }

    private static record IPResponse(String ip) {
    }

    public void getIP(Context ctx) throws Exception {
        var ip = LocalIP.getNetworkIp();
        String suffix = String.format(":%d", port);
        ctx.json(new IPResponse(ip.toString() + suffix));
    }
}
