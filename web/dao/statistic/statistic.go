package statisticdao

import (
	"database/sql"
	"fmt"
	"time"

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

type MaxPlayerCountDay struct {
	Date           time.Time
	MaxPlayerCount int
}

func GetMaxPlayerCountForDays(db *sql.DB, server models.Server, dayLimit int) (*[]MaxPlayerCountDay, error) {
	query := fmt.Sprintf(`
		SELECT DATE(created_at) AS date, MAX(player_count) AS max_player_count
		FROM statistics
		WHERE server_id = ? AND created_at >= DATE_SUB(CURDATE(), INTERVAL %d DAY)
		GROUP BY DATE(created_at)
		ORDER BY date
	`, dayLimit)

	rows, err := db.Query(query, server.Id)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var result []MaxPlayerCountDay

	for rows.Next() {
		if err := rows.Err(); err != nil {
			return nil, err
		}

		var date string
		var maxPlayerCount MaxPlayerCountDay
		if err := rows.Scan(&date, &maxPlayerCount.MaxPlayerCount); err != nil {
			return nil, err
		}

		dateTime, err := time.Parse("2006-01-02", date)
		if err != nil {
			return nil, err
		}
		maxPlayerCount.Date = dateTime

		result = append(result, maxPlayerCount)
	}

	return &result, nil
}

type PlayerCountDay struct {
	Date        time.Time
	PlayerCount int
}

func GetPlayerCountHistory(db *sql.DB, server models.Server, hourLimit int) (*[]PlayerCountDay, error) {
	query := fmt.Sprintf(`
		SELECT created_at as Date, player_count as PlayerCount
		FROM statistics
		WHERE server_id = ?
		ORDER BY date
		LIMIT %d
	`, hourLimit)

	rows, err := db.Query(query, server.Id)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var result []PlayerCountDay

	for rows.Next() {
		if err := rows.Err(); err != nil {
			return nil, err
		}

		var date string
		var playerCount PlayerCountDay
		if err := rows.Scan(&date, &playerCount.PlayerCount); err != nil {
			return nil, err
		}

		dateTime, err := time.Parse("2006-01-02", date)
		if err != nil {
			return nil, err
		}
		playerCount.Date = dateTime

		result = append(result, playerCount)
	}

	return &result, nil
}
