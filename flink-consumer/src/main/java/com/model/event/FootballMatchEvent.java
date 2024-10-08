package com.model.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FootballMatchEvent {
    private String id, matchId, homeTeam, awayTeam, eventName, eventAssociatedTeam, gameTime;
    private long eventTime, homePossessionTime, awayPossessionTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventAssociatedTeam() {
        return eventAssociatedTeam;
    }

    public void setEventAssociatedTeam(String eventAssociatedTeam) {
        this.eventAssociatedTeam = eventAssociatedTeam;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public long getHomePossessionTime() {
        return homePossessionTime;
    }

    public void setHomePossessionTime(Long homePossessionTime) {
        this.homePossessionTime = homePossessionTime;
    }

    public long getAwayPossessionTime() {
        return awayPossessionTime;
    }

    public void setAwayPossessionTime(Long awayPossessionTime) {
        this.awayPossessionTime = awayPossessionTime;
    }
}

