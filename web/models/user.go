package models

import "time"

type User struct {
	Id        *int64
	Username  string
	Email     string
	UpdatedAt *time.Time
	CreatedAt *time.Time
}
