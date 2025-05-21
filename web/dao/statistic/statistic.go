package statisticdao

import (
	"database/sql"

	"github.com/dinkelspiel/goldenage/web/models"
)

func CreateStatistic(db *sql.DB, statistic models.Statistic) (*models.Statistic, error) {
	insertStatistic := "INSERT INTO statistics(plugin_id, player_count, online_mode, game_version, server_environment, public_ip, operating_system, arch, java_version) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"

	res, err := db.Exec(insertStatistic, statistic.PluginId, statistic.PlayerCount, statistic.OnlineMode, statistic.GameVersion, statistic.ServerEnvironment, statistic.PublicIp, statistic.OperatingSystem, statistic.Arch, statistic.JavaVersion)
	if err != nil {
		return nil, err
	}
	id, _ := res.LastInsertId()

	result := statistic
	result.Id = &id
	return &result, nil
}
