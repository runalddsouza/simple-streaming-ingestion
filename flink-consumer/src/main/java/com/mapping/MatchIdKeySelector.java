package com.mapping;

import com.model.event.FootballMatchEvent;
import org.apache.flink.api.java.functions.KeySelector;

public class MatchIdKeySelector implements KeySelector<FootballMatchEvent, String> {
    @Override
    public String getKey(FootballMatchEvent footballMatchEvent) {
        return footballMatchEvent.getMatchId();
    }
}
