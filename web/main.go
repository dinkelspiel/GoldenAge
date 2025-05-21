package main

import (
	"database/sql"
	"log"
	"net/http"

	plugindao "github.com/dinkelspiel/goldenage/web/dao/plugin"
	statisticdao "github.com/dinkelspiel/goldenage/web/dao/statistic"
	"github.com/dinkelspiel/goldenage/web/models"
	"github.com/gin-gonic/gin"
	_ "github.com/go-sql-driver/mysql"
)

type PostStatisticBody struct {
	PluginId     int64  `json:"pluginId"`
	PluginSecret string `json:"pluginSecret"`

	PlayerCount       int    `json:"playerCount"`
	OnlineMode        bool   `json:"onlineMode"`
	GameVersion       string `json:"gameVersion"`
	ServerEnvironment string `json:"serverEnvironment"`
	PublicIp          string `json:"publicIp"`
	OperatingSystem   string `json:"operatingSystem"`
	Arch              string `json:"arch"`
	JavaVersion       string `json:"javaVersion"`
}

func setupRouter(db *sql.DB) *gin.Engine {
	// Disable Console Color
	// gin.DisableConsoleColor()
	r := gin.Default()

	// Ping test
	r.GET("/ping", func(c *gin.Context) {
		c.String(http.StatusOK, "pong")
	})

	r.POST("/api/statistics", func(c *gin.Context) {
		var body PostStatisticBody
		if err := c.BindJSON(&body); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		_, err := plugindao.GetPluginByIdAndSecret(db, body.PluginId, body.PluginSecret)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{
				"error": err.Error(),
			})
			return
		}

		createStatistic := models.Statistic{
			PlayerCount:       body.PlayerCount,
			OnlineMode:        body.OnlineMode,
			GameVersion:       body.GameVersion,
			ServerEnvironment: body.ServerEnvironment,
			PublicIp:          body.PublicIp,
			OperatingSystem:   body.OperatingSystem,
			Arch:              body.Arch,
			JavaVersion:       body.JavaVersion,
			PluginId:          body.PluginId,
		}
		_, err = statisticdao.CreateStatistic(db, createStatistic)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{
				"error": err.Error(),
			})
			return
		}

		c.JSON(http.StatusCreated, gin.H{
			"message": "Created statistic",
		})
	})

	// Get user value
	// r.GET("/user/:name", func(c *gin.Context) {

	// })

	// Authorized group (uses gin.BasicAuth() middleware)
	// Same than:
	// authorized := r.Group("/")
	// authorized.Use(gin.BasicAuth(gin.Credentials{
	//	  "foo":  "bar",
	//	  "manu": "123",
	//}))
	// authorized := r.Group("/", gin.BasicAuth(gin.Accounts{
	// 	"foo":  "bar", // user:foo password:bar
	// 	"manu": "123", // user:manu password:123
	// }))

	/* example curl for /admin with basicauth header
	   Zm9vOmJhcg== is base64("foo:bar")

		curl -X POST \
	  	http://localhost:8080/admin \
	  	-H 'authorization: Basic Zm9vOmJhcg==' \
	  	-H 'content-type: application/json' \
	  	-d '{"value":"bar"}'
	*/
	// authorized.POST("admin", func(c *gin.Context) {
	// 	user := c.MustGet(gin.AuthUserKey).(string)

	// 	// Parse JSON
	// 	var json struct {
	// 		Value string `json:"value" binding:"required"`
	// 	}

	// 	if c.Bind(&json) == nil {
	// 		db[user] = json.Value
	// 		c.JSON(http.StatusOK, gin.H{"status": "ok"})
	// 	}
	// })

	return r
}

func main() {
	dsn := "golden:golden@tcp(mysql:3306)/golden"
	db, err := sql.Open("mysql", dsn)
	if err != nil {
		log.Fatal("Error opening database: ", err)
	}
	defer db.Close()
	if err = db.Ping(); err != nil {
		log.Fatal("Error pinging database: ", err)
	}

	r := setupRouter(db)
	// Listen and Server in 0.0.0.0:8080
	r.Run(":8080")
}
