.PHONY: test test-coverage test-bench

test:
	go test -v ./...

test-coverage:
	go test -v -coverprofile=coverage.out ./...
	go tool cover -html=coverage.out -o coverage.html
	@echo "Coverage report generated: coverage.html"

test-bench:
	go test -v -bench=. -benchmem ./...
