package com.warp;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IPRouteIterator implements Iterator<IPv4> {
    private final IPv4 ip;
    private int count;
    private final int MAX_COUNT = 256;

    public IPRouteIterator(IPv4 ip) {
        this.ip = ip;
        count = 2; // 0/1 are generally router config pages.
    }

    @Override
    public boolean hasNext() {
        if (count == MAX_COUNT) {
            return false;
        }

        return true;
    }

    @Override
    public IPv4 next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        // skip self.
        if (count == ip.getParts()[3]) {
            count++;
        }

        int[] parts = ip.getParts();
        parts[3] = count;
        count++;
        return new IPv4(parts.clone());
    }
}
