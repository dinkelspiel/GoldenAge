package plugindao

import (
	"database/sql"
	"errors"
	"time"

	userdao "github.com/dinkelspiel/goldenage/web/dao/user"
	"github.com/dinkelspiel/goldenage/web/models"
)

func GetPluginByIdAndSecret(db *sql.DB, id int64, secret string) (*models.Plugin, error) {
	rows, err := db.Query("SELECT id, name, secret, user_id, created_at, updated_at FROM plugins WHERE id = ? AND secret = ?", id, secret)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var plugin models.Plugin
	if rows.Next() {
		var createdAt, updatedAt string
		if err := rows.Scan(&plugin.Id, &plugin.Name, &plugin.Secret, &plugin.UserId, &createdAt, &updatedAt); err != nil {
			return nil, err
		}
		updatedAtTime, err := time.Parse("2006-01-02 15:04:05", updatedAt)
		if err != nil {
			return nil, err
		}
		createdAtTime, err := time.Parse("2006-01-02 15:04:05", createdAt)
		if err != nil {
			return nil, err
		}
		plugin.UpdatedAt = &updatedAtTime
		plugin.CreatedAt = &createdAtTime
		plugin.User, err = userdao.GetUserById(db, plugin.UserId)
		if err != nil {
			return nil, err
		}
		return &plugin, nil
	} else {
		return nil, errors.New("no plugins found with matching id and secret")
	}
}
