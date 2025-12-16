package files

import (
	"encoding/json"
	"fmt"
	"io"
	"log/slog"
	"net/http"
)

type FilesController struct {
	logger       *slog.Logger
	filesService *FilesService
}

func NewFilesController(logger *slog.Logger, filesService *FilesService) *FilesController {
	return &FilesController{logger, filesService}
}

func (controller FilesController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /api/files", controller.ListFiles)
	mux.HandleFunc("GET /api/files/{filename}", controller.DownloadFile)
}

type ListFilesResponse struct {
	Files []string `json:"files"`
}

func (controller FilesController) ListFiles(w http.ResponseWriter, r *http.Request) {
	files, err := controller.filesService.ListFiles()
	if err != nil {
		controller.logger.Error("failed to list directory content",
			"error", err.Error(),
		)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	res := ListFilesResponse{files}
	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(res); err != nil {
		controller.logger.Error("failed to write ListFiles response",
			"error", err.Error(),
		)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
}

func (controller FilesController) DownloadFile(w http.ResponseWriter, r *http.Request) {
	filename := r.PathValue("filename")
	file, err := controller.filesService.OpenFile(filename)
	if err != nil {
		controller.logger.Error("failed to open file",
			"filename", filename,
			"error", err.Error(),
		)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	defer func() {
		if err := file.Close(); err != nil {
			controller.logger.Error("failed to close file",
				"filename", filename,
				"error", err.Error(),
			)
		}
	}()

	w.Header().Set("Content-Type", "application/octet-stream")
	w.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=\"%s\"", filename))

	if _, err = io.Copy(w, file); err != nil {
		controller.logger.Error("failed to stream file",
			"error", err.Error(),
		)
	}
}
