package files

import (
	"errors"
	"fmt"
	"log/slog"
	"os"
)

type FilesService struct {
	logger     *slog.Logger
	currentDir string
}

func NewFilesService(logger *slog.Logger, currentDir string) *FilesService {
	return &FilesService{logger, currentDir}
}

func (service FilesService) ListFiles() ([]string, error) {
	files, err := os.ReadDir(service.currentDir)
	if err != nil {
		return nil, fmt.Errorf("failed to list directory content: %w", err)
	}

	result := []string{}
	for _, entry := range files {
		if !entry.IsDir() {
			result = append(result, entry.Name())
		}
	}

	return result, nil
}

func (service FilesService) OpenFile(filename string) (*os.File, error) {
	if filename == "" {
		return nil, errors.New("filename is missing")
	}

	filePath := fmt.Sprintf("%s/%s", service.currentDir, filename)
	file, err := os.Open(filePath)
	if err != nil {
		service.logger.Error("failed to open file",
			"error", err.Error(),
		)
		return nil, errors.New("failed to access requested file")
	}

	return file, nil
}
