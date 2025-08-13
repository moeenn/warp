package com.warp.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class FilesController implements Controller {
    private final String currentDirectory;

    public FilesController(String currentDir) {
        currentDirectory = currentDir;
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.get("/api/files", this::listFiles);
        app.get("/api/files/{filename}", this::getFile);
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

    public void getFile(Context ctx) throws Exception {
        String filename = ctx.pathParam("filename");
        var file = new File(currentDirectory + "/" + filename);
        if (!file.exists() || !file.isFile()) {
            var res = new ErrorResponse("file not found");
            ctx.status(404).json(res);
            return;
        }

        String mimeType = URLConnection.guessContentTypeFromName(filename);
        String headerValue = (mimeType != null)
                ? mimeType
                : "application/octet-stream";

        ctx.header("Content-Type", headerValue);
        ctx.header("Content-Length", String.valueOf(file.length()));

        InputStream stream = new FileInputStream(file);
        ctx.result(stream);
    }
}
