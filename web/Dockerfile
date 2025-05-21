# Build stage
FROM alpine:3.21

RUN apk add "go=~1.23"

# Set working directory inside container
WORKDIR /app

# Copy go module files first (to leverage Docker layer caching)
COPY go.mod go.sum ./
RUN go mod download

# Copy the rest of the app source code
COPY . .

# Build the Go application
# CMD ["go", "build", "-o", "main", "."]
RUN go build -o main .

# Expose port 8080
EXPOSE 8080

# Command to run the application
CMD ["./main"]
