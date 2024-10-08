package com.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.event.FootballMatchEvent;
import com.model.event.FootballMatchEventType;
import com.model.table.FootballMatchStatType;
import com.model.table.FootballMatchStats;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.typeinfo.Types;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FootballMatchStatsMapper extends RichMapFunction<FootballMatchEvent, FootballMatchStats> {

    private StateTtlConfig stateTtlConfig;
    private MapState<String, Map<String, String>> matchState;

    public FootballMatchStatsMapper() {
        stateTtlConfig = StateTtlConfig.newBuilder(Duration.ofMinutes(90)).build();
    }

    public void open(OpenContext ctx) {
        MapStateDescriptor<String, Map<String, String>> matchState = new MapStateDescriptor<>("matchStats", Types.STRING, Types.MAP(Types.STRING, Types.STRING));
        matchState.enableTimeToLive(stateTtlConfig);
        this.matchState = getRuntimeContext().getMapState(matchState);

    }

    String[] calculatePossession(long homeTime, long awayTime) {
        double total = homeTime + awayTime;
        if (total == 0)
            return new String[]{"0 %", "0 %"};
        double homeTimePercent = (Math.round((homeTime / total) * 100.0 * 100)) / 100.0;
        double awayTimePercent = (Math.round((awayTime / total) * 100.0 * 100)) / 100.0;
        return new String[]{homeTimePercent + " %", awayTimePercent + " %"};
    }

    String calculatePassSuccess(String team) throws Exception {
        double pass = Double.parseDouble(matchState.get(team).get(FootballMatchEventType.PASS.name()));
        int passComplete = Integer.parseInt(matchState.get(team).get(FootballMatchEventType.PASS_COMPLETE.name()));
        if (pass == 0)
            return "0 %";
        double percent = (Math.round((passComplete / pass) * 100.0 * 100)) / 100.0;
        return percent + " %";
    }


    private Map<String, String> getInitialStats() {
        Map<String, String> stats = new HashMap<>();
        for (FootballMatchEventType eventType : FootballMatchEventType.values()) {
            stats.put(eventType.name(), "0");
        }
        return stats;
    }


    @Override
    public FootballMatchStats map(FootballMatchEvent footballMatchEvent) throws Exception {
        String homeTeam = footballMatchEvent.getHomeTeam();
        String awayTeam = footballMatchEvent.getAwayTeam();
        String currentTeam = footballMatchEvent.getEventAssociatedTeam();

        if (!matchState.contains(homeTeam)) {
            matchState.put(homeTeam, getInitialStats());
        }
        if (!matchState.contains(awayTeam)) {
            matchState.put(awayTeam, getInitialStats());
        }

        // Update stats
        matchState.get(currentTeam)
                  .put(footballMatchEvent.getEventName(), String.valueOf(Integer.parseInt(matchState.get(currentTeam)
                                                                                                    .get(footballMatchEvent.getEventName())) + 1));
        // Ball Possession
        String[] ballPos = calculatePossession(footballMatchEvent.getHomePossessionTime(), footballMatchEvent.getAwayPossessionTime());
        matchState.get(homeTeam).put(FootballMatchStatType.POSSESSION.name(), ballPos[0]);
        matchState.get(awayTeam).put(FootballMatchStatType.POSSESSION.name(), ballPos[1]);

        // Pass Success
        matchState.get(homeTeam).put(FootballMatchStatType.PASS_PERCENT.name(), calculatePassSuccess(homeTeam));
        matchState.get(awayTeam).put(FootballMatchStatType.PASS_PERCENT.name(), calculatePassSuccess(awayTeam));

        FootballMatchStats footballMatchStats = new FootballMatchStats();
        footballMatchStats.setMatchId(footballMatchEvent.getMatchId());
        footballMatchStats.setGameTime(footballMatchEvent.getGameTime());
        footballMatchStats.setHomeTeam(homeTeam);
        footballMatchStats.setHomeStats(new ObjectMapper().writeValueAsString(matchState.get(homeTeam)));
        footballMatchStats.setAwayTeam(awayTeam);
        footballMatchStats.setAwayStats(new ObjectMapper().writeValueAsString(matchState.get(awayTeam)));
        return footballMatchStats;
    }
}
