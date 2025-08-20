package main

import (
	"fmt"
	"log/slog"
	"net/http"
	"os"
	"time"
	"warp/internal/files"
	"warp/internal/ip"
	"warp/internal/middleware"
)

const (
	Port          int           = 8080
	Address       string        = "localhost:8080"
	ServerTimeout time.Duration = time.Second * 20
)

func run() error {
	logger := slog.New(slog.NewJSONHandler(os.Stdout, nil))
	mux := http.NewServeMux()
	fs := http.FileServer(http.Dir("./public"))
	mux.Handle("/", http.StripPrefix("/public", fs))

	currentDir, err := os.Getwd()
	if err != nil {
		return fmt.Errorf("failed to determine current dir: %w", err)
	}

	filesService := files.NewFilesService(logger, currentDir)
	filesController := files.NewFilesController(logger, filesService)

	ipService := ip.NewIpService(logger)
	ipController := ip.NewIpController(logger, Port, ipService)

	ipController.RegisterRoutes(mux)
	filesController.RegisterRoutes(mux)

	//nolint: exhaustruct
	server := &http.Server{
		Addr:              Address,
		Handler:           middleware.Cors(mux),
		ReadTimeout:       ServerTimeout,
		WriteTimeout:      ServerTimeout,
		IdleTimeout:       ServerTimeout,
		ReadHeaderTimeout: ServerTimeout,
	}

	logger.Info("starting server", "address", Address)
	return server.ListenAndServe()
}

func main() {
	if err := run(); err != nil {
		fmt.Fprintf(os.Stderr, "error: %s\n", err.Error())
		os.Exit(1)
	}
}
