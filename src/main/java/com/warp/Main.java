package com.warp;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.warp.controllers.FilesController;
import com.warp.controllers.HomeController;
import com.warp.controllers.IPController;
import io.javalin.Javalin;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            /** enable static file rendering */
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/frontend/dist";
            });

            /** enable cors */
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });
        });

        Path currentDir = Paths.get("").toAbsolutePath();
        var homeController = new HomeController();
        var ipController = new IPController(PORT);
        var filesControler = new FilesController(currentDir.toString());

        app.get("/api/greet", ctx -> homeController.greet(ctx));
        app.get("/api/local-ip", ctx -> ipController.getIP(ctx));
        app.get("/api/files", ctx -> filesControler.listFiles(ctx));

        app.start(PORT);
    }
}
