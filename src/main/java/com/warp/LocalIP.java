package com.warp;

import java.net.Socket;
import java.net.InetSocketAddress;

public class LocalIP {
    public static IPv4 getNetworkIp() throws Exception {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            String rawIp = socket.getLocalAddress().toString();
            return IPv4.fromString(rawIp);
        }
    }
}