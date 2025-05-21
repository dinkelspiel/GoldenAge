package models

import (
	"time"
)

type Plugin struct {
	Id        *int64
	Name      string
	Secret    string
	UserId    int64
	User      *User
	UpdatedAt *time.Time
	CreatedAt *time.Time
}
