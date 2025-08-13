package com.warp;

import java.nio.file.Path;
import java.nio.file.Paths;
import com.warp.controllers.FilesController;
import com.warp.controllers.IPController;
import com.warp.services.IPService;
import io.javalin.Javalin;
import io.javalin.config.SizeUnit;

public class Main {
    private static final Path currentDir = Paths.get("").toAbsolutePath();
    private static final int PORT = System.getenv("PORT") != null
            ? Integer.parseInt(System.getenv("PORT"))
            : 8080;

    public static void main(String[] args) {
        /** turn off javalin startup logging */
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "WARN");

        var app = Javalin.create(config -> {
            /** enable static file rendering */
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/frontend";
            });

            /** enable cors */
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });

            config.jetty.multipartConfig.cacheDirectory(currentDir + "/tmp");
            config.jetty.multipartConfig.maxFileSize(5, SizeUnit.MB);
            config.jetty.multipartConfig.maxInMemoryFileSize(6, SizeUnit.MB);
            config.jetty.multipartConfig.maxTotalRequestSize(6, SizeUnit.MB);
        });

        var ipService = new IPService();

        var ipController = new IPController(ipService, PORT);
        ipController.registerRoutes(app);

        var filesController = new FilesController(currentDir.toString());
        filesController.registerRoutes(app);

        System.out.printf("Browse to URL: http://localhost:%d\n", PORT);
        app.start(PORT);
    }
}
