package pages

import (
	"html/template"
	"log/slog"
	"net/http"
)

type PagesController struct {
	logger    *slog.Logger
	templates *template.Template
}

func NewPagesController(logger *slog.Logger, templates *template.Template) *PagesController {
	return &PagesController{
		logger:    logger,
		templates: templates,
	}
}

func (c *PagesController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("/", c.HomePage)
}

func (c *PagesController) HomePage(w http.ResponseWriter, r *http.Request) {
	if err := c.templates.ExecuteTemplate(w, "home_page.html", nil); err != nil {
		c.logger.Error("failed to execute template",
			"error", err.Error(),
		)
		w.WriteHeader(http.StatusInternalServerError)
	}
}
