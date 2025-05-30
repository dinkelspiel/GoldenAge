package plugindao

import (
	"database/sql"
	"errors"
	"time"

	userdao "github.com/dinkelspiel/goldenage/web/dao/user"
	"github.com/dinkelspiel/goldenage/web/models"
)

func scanServerRow(rows *sql.Rows, db *sql.DB) (*models.Server, error) {
	var server models.Server
	var createdAt string
	var updatedAt sql.NullString

	if err := rows.Scan(&server.Id, &server.Name, &server.Secret, &server.ServerAddress, &server.UserId, &createdAt, &updatedAt); err != nil {
		return nil, err
	}

	createdAtTime, err := time.Parse("2006-01-02 15:04:05", createdAt)
	if err != nil {
		return nil, err
	}
	server.CreatedAt = &createdAtTime

	if updatedAt.Valid {
		updatedAtTime, err := time.Parse("2006-01-02 15:04:05", updatedAt.String)
		if err != nil {
			return nil, err
		}
		server.UpdatedAt = &updatedAtTime
	} else {
		server.UpdatedAt = nil
	}

	server.User, err = userdao.GetUserById(db, server.UserId)
	if err != nil {
		return nil, err
	}

	return &server, nil
}

func GetServerByIdAndSecret(db *sql.DB, id int64, secret string) (*models.Server, error) {
	rows, err := db.Query("SELECT id, name, secret, server_address, user_id, created_at, updated_at FROM servers WHERE id = ? AND secret = ?", id, secret)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	if rows.Next() {
		return scanServerRow(rows, db)
	}
	return nil, errors.New("no servers found with matching id and secret")
}

func GetServerById(db *sql.DB, id int64) (*models.Server, error) {
	rows, err := db.Query("SELECT id, name, secret, server_address, user_id, created_at, updated_at FROM servers WHERE id = ?", id)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	if rows.Next() {
		return scanServerRow(rows, db)
	}
	return nil, errors.New("no servers found with matching id")
}
