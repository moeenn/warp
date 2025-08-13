package com.warp;

import java.util.List;

public class Main {
    static void run() throws Exception {
        IPv4 localIp = LocalIP.getNetworkIp();
        IPRoute route = new IPRoute(localIp);

        List<IPv4> aliveIPs = route.findAliveIPs();
        System.out.printf("\n\n -- Scan results -- \n");
        for (var ip : aliveIPs) {
            System.out.println(ip);
        }
    }

    public static void main(String[] args) {
        try {
            Main.run();
        } catch (Exception ex) {
            System.out.println("error: " + ex.getMessage());
        }
    }
}
