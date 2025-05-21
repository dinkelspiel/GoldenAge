package models

import "time"

type Statistic struct {
	Id *int64

	PlayerCount       int
	OnlineMode        bool
	GameVersion       string
	ServerEnvironment string
	PublicIp          string
	OperatingSystem   string
	Arch              string
	JavaVersion       string

	PluginId int64
	Plugin   Plugin

	UpdatedAt *time.Time
	CreatedAt *time.Time
}
