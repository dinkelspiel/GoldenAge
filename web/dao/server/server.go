package plugindao

import (
	"database/sql"
	"errors"
	"time"

	userdao "github.com/dinkelspiel/goldenage/web/dao/user"
	"github.com/dinkelspiel/goldenage/web/models"
)

func GetServerByIdAndSecret(db *sql.DB, id int64, secret string) (*models.Server, error) {
	rows, err := db.Query("SELECT id, name, secret, server_address, user_id, created_at, updated_at FROM servers WHERE id = ? AND secret = ?", id, secret)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var server models.Server
	if rows.Next() {
		var createdAt string
		var updatedAt sql.NullString
		if err := rows.Scan(&server.Id, &server.Secret, &server.Name, &server.ServerAddress, &server.UserId, &createdAt, &updatedAt); err != nil {
			return nil, err
		}
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
			server.UpdatedAt = &updatedAtTime
		} else {
			server.UpdatedAt = nil
		}
		server.CreatedAt = &createdAtTime
		server.User, err = userdao.GetUserById(db, server.UserId)
		if err != nil {
			return nil, err
		}
		return &server, nil
	} else {
		return nil, errors.New("no servers found with matching id and secret")
	}
}
