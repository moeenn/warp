package com.warp;

public class IPScanStatus {
    private int total;
    private int completed;

    IPScanStatus() {
        total = 0;
        completed = 0;
    }

    void incrementTotal() {
        total++;
    }

    void incrementCompleted() {
        completed++;
        float perc = ((float) completed / (float) total) * 100;
        System.out.printf("\rProgress: %3.2f%%", perc);
    }
}
