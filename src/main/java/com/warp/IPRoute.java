package com.warp;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IPRoute implements Iterable<IPv4> {
    private final IPv4 ip;
    private final int TIMEOUT = 2_000;

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
            return InetAddress.getByName(ip.toString()).isReachable(TIMEOUT);
        } catch (Exception ex) {
            System.err.println("error: host error: " + ex.getMessage());
            return false;
        }
    }

    public List<IPv4> findAliveIPs() {
        List<IPv4> aliveIPs = new ArrayList<IPv4>();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IPScanStatus status = new IPScanStatus();
            for (IPv4 ip : this) {
                status.incrementTotal();
                executor.submit(() -> {
                    if (isIPAlive(ip)) {
                        aliveIPs.add(ip);
                    }
                    status.incrementCompleted();
                });
            }

            executor.shutdown();
        } catch (Exception ex) {
            System.err.println("error: " + ex.getMessage());
            System.exit(1);
        }

        return aliveIPs;
    }
}
