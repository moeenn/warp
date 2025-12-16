package ip

import (
	"encoding/json"
	"fmt"
	"log/slog"
	"net/http"
)

type IpController struct {
	logger    *slog.Logger
	port      int
	ipService *IpService
}

func NewIpController(logger *slog.Logger, port int, ipService *IpService) *IpController {
	return &IpController{logger, port, ipService}
}

func (controller IpController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /api/local-ip", controller.GetIp)
}

type IpResponse struct {
	Ip string `json:"ip"`
}

func (controller IpController) GetIp(w http.ResponseWriter, r *http.Request) {
	ip, err := controller.ipService.GetOutboundIP()
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	res := IpResponse{
		Ip: fmt.Sprintf("%s:%d", ip.To4().String(), controller.port),
	}

	w.Header().Set("Content-Type", "application/json")
	if err := json.NewEncoder(w).Encode(res); err != nil {
		controller.logger.Error("failed to write GetIp response",
			"error", err.Error(),
		)
		w.WriteHeader(http.StatusInternalServerError)
		return
	}
}
