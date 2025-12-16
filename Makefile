ENTRYPOINT=./cmd/warp/main.go


dev:
	go run ${ENTRYPOINT}


.PHONY: dev
