package com.warp.services;

import java.net.InetSocketAddress;
import java.net.Socket;

public class IPService {
    public String getNetworkIp() throws Exception {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            String address = socket.getLocalAddress().toString();
            return address.substring(1);
        }
    }
}
