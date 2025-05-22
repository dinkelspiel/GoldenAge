package models

import (
	"time"
)

type Server struct {
	Id            *int64
	Name          string
	Secret        string
	ServerAddress string
	UserId        int64
	User          *User
	UpdatedAt     *time.Time
	CreatedAt     *time.Time
}
