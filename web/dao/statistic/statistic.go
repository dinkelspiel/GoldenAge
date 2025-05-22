package statisticdao

import (
	"database/sql"

	"github.com/dinkelspiel/goldenage/web/models"
)

func CreateStatistic(db *sql.DB, statistic models.Statistic) (*models.Statistic, error) {
	insertStatistic := "INSERT INTO statistics(server_id, player_count, game_version, server_environment, operating_system, arch, java_version) VALUES(?, ?, ?, ?, ?, ?, ?)"

	res, err := db.Exec(insertStatistic, statistic.ServerId, statistic.PlayerCount, statistic.GameVersion, statistic.ServerEnvironment, statistic.OperatingSystem, statistic.Arch, statistic.JavaVersion)
	if err != nil {
		return nil, err
	}
	id, _ := res.LastInsertId()

	result := statistic
	result.Id = &id
	return &result, nil
}
