package ip

import (
	"errors"
	"log/slog"
	"net"
)

type IpService struct {
	logger *slog.Logger
}

func NewIpService(logger *slog.Logger) *IpService {
	return &IpService{logger}
}

func (service IpService) GetOutboundIP() (net.IP, error) {
	conn, err := net.Dial("udp", "8.8.8.8:80")
	if err != nil {
		service.logger.Error("failed to detemine ip address",
			"error", err.Error(),
		)
		return nil, errors.New("failed to determine IP address")
	}
	defer conn.Close()

	localAddr := conn.LocalAddr().(*net.UDPAddr)
	return localAddr.IP, nil
}
