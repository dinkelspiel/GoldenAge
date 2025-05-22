package models

import "time"

type Statistic struct {
	Id *int64

	PlayerCount       int
	GameVersion       string
	ServerEnvironment string
	OperatingSystem   string
	Arch              string
	JavaVersion       string

	ServerId *int64
	Server   *Server

	UpdatedAt *time.Time
	CreatedAt *time.Time
}
