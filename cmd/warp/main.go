package main

import (
	"fmt"
	"html/template"
	"log/slog"
	"net/http"
	"os"
	"warp/internal/files"
	"warp/internal/ip"
	"warp/internal/middleware"
	"warp/internal/pages"
)

const (
	PORT    int    = 3000
	ADDRESS string = "localhost:3000"
)

func run() error {
	logger := slog.New(slog.NewJSONHandler(os.Stdout, nil))
	templates, err := template.ParseGlob("views/**/*.html")
	if err != nil {
		return fmt.Errorf("failed to parse templates: %w", err)
	}

	mux := http.NewServeMux()
	fs := http.FileServer(http.Dir("./public"))
	mux.Handle("/public/", http.StripPrefix("/public", fs))

	currentDir, err := os.Getwd()
	if err != nil {
		return fmt.Errorf("failed to determine current dir: %w", err)
	}

	pagesController := pages.NewPagesController(logger, templates)

	filesService := files.NewFilesService(logger, currentDir)
	filesController := files.NewFilesController(logger, filesService)

	ipService := ip.NewIpService(logger)
	ipController := ip.NewIpController(logger, PORT, ipService)

	pagesController.RegisterRoutes(mux)
	ipController.RegisterRoutes(mux)
	filesController.RegisterRoutes(mux)

	//nolint: exhaustruct
	server := &http.Server{
		Addr:    ADDRESS,
		Handler: middleware.Cors(mux),
	}

	logger.Info("starting server", "address", ADDRESS)
	return server.ListenAndServe()
}

func main() {
	if err := run(); err != nil {
		fmt.Fprintf(os.Stderr, "error: %s\n", err.Error())
		os.Exit(1)
	}
}
