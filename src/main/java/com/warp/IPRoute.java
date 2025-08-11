package com.warp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IPRoute implements Iterable<IPv4> {
    private final IPv4 ip;
    private final int TIMEOUT = 20_000;
    private final int MAX_PARALLEL = 50;

    IPRoute(IPv4 ip) {
        this.ip = ip;
    }

    public IPv4 defaultRoute() {
        int[] parts = ip.getParts();
        parts[3] = 0;
        return new IPv4(parts);
    }

    public IPRouteIterator iterator() {
        return new IPRouteIterator(ip);
    }

    private boolean isIPAlive(IPv4 ip) {
        try {
            System.out.println("checking ip: " + ip.toString());
            return InetAddress.getByName(ip.toString()).isReachable(TIMEOUT);
        } catch (Exception ex) {
            System.err.println("error: host error: " + ex.getMessage());
            return false;
        }
    }

    public List<IPv4> findAliveIPs() {
        List<IPv4> aliveIPs = new ArrayList<IPv4>();
        try (ExecutorService executor = Executors.newFixedThreadPool(MAX_PARALLEL, Thread.ofVirtual().factory())) {
            for (IPv4 ip : this) {
                executor.submit(() -> {
                    if (isIPAlive(ip)) {
                        aliveIPs.add(ip);
                    }
                });
                
            }
                        
            executor.shutdown();
            executor.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception ex) {
            System.err.println("error: " + ex.getMessage());
            System.exit(1);
        }

        return aliveIPs;
    }
}
