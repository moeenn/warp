package com.warp;

public class IPv4 {
    private final int[] parts;

    public IPv4(int[] parts) {
        assert parts.length == 4;
        this.parts = parts;
    }

    public static IPv4 fromString(String input) throws Exception {
        String[] pieces = input.split("\\.");
        if (pieces.length != 4) {
            throw new Exception("invalid ip address provided: " + input);
        }

        int[] parsedPieces = new int[4];
        for (int i = 0; i < 4; i++) {
            String piece = pieces[i];
            if (piece.startsWith("/")) {
                piece = piece.substring(1);
            }

            parsedPieces[i] = Integer.parseInt(piece);
        }

        return new IPv4(parsedPieces);
    }

    public int[] getParts() {
        return parts;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            builder.append(parts[i]);
            if (i != 3) {
                builder.append(".");
            }
        }

        return builder.toString();
    }
}