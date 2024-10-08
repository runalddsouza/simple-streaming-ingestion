package com.mapping;

import com.model.table.FootballMatchStats;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.table.data.GenericRowData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.data.binary.BinaryStringData;
import org.apache.flink.types.RowKind;

public class FootballMatchStatTableMapper implements MapFunction<FootballMatchStats, RowData> {
    @Override
    public RowData map(FootballMatchStats footballMatchStats) {
        GenericRowData genericRowData = new GenericRowData(RowKind.UPDATE_AFTER, 10);
        genericRowData.setField(0, BinaryStringData.fromString(footballMatchStats.getMatchId()));
        genericRowData.setField(1, BinaryStringData.fromString(footballMatchStats.getGameTime()));
        genericRowData.setField(2, BinaryStringData.fromString(footballMatchStats.getHomeTeam()));
        genericRowData.setField(3, BinaryStringData.fromString(footballMatchStats.getHomeStats()));
        genericRowData.setField(4, BinaryStringData.fromString(footballMatchStats.getAwayTeam()));
        genericRowData.setField(5, BinaryStringData.fromString(footballMatchStats.getAwayStats()));
        return genericRowData;
    }
}
