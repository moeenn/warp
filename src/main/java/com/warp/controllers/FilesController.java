package com.warp.controllers;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.javalin.http.Context;

public class FilesController {
    private final String currentDirectory;

    public FilesController(String currentDir) {
        currentDirectory = currentDir;
    }

    public static record FilesListResponse(
            List<String> files) {
    }

    public void listFiles(Context ctx) throws Exception {
        List<String> filenames = Stream.of(new File(currentDirectory).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());

        ctx.json(new FilesListResponse(filenames));
    }
}
