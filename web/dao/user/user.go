package userdao

import (
	"database/sql"
	"time"

	"github.com/dinkelspiel/goldenage/web/models"
)

func GetUserById(db *sql.DB, userId int64) (*models.User, error) {
	var createdAt string
	var updatedAt sql.NullString
	rows, err := db.Query("SELECT id, username, email, updated_at, created_at FROM users WHERE id = ?", userId)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var user models.User
	if rows.Next() {
		if err := rows.Scan(&user.Id, &user.Username, &user.Email, &updatedAt, &createdAt); err != nil {
			return nil, err
		} else {
			var updatedAtTime time.Time
			if updatedAt.Valid {
				updatedAtTime, err = time.Parse("2006-01-02 15:04:05", updatedAt.String)
				if err != nil {
					return nil, err
				}
			}
			createdAtTime, err := time.Parse("2006-01-02 15:04:05", createdAt)
			if err != nil {
				return nil, err
			}
			if updatedAt.Valid {
				user.UpdatedAt = &updatedAtTime
			} else {
				user.UpdatedAt = nil
			}
			user.CreatedAt = &createdAtTime
			return &user, nil
		}
	}
	return nil, err
}
