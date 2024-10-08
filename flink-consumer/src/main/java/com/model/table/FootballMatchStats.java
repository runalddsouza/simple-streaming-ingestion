package com.model.table;


public class FootballMatchStats {

    private String matchId, gameTime, homeTeam, awayTeam, homeStats, awayStats;


    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getHomeStats() {
        return homeStats;
    }

    public void setHomeStats(String homeStats) {
        this.homeStats = homeStats;
    }

    public String getAwayStats() {
        return awayStats;
    }

    public void setAwayStats(String awayStats) {
        this.awayStats = awayStats;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }
}
