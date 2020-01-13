package com.brewingmadscientists.birdhouseagent.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@RequiredArgsConstructor
public class BirdHouseAgentHeartbeat {
    private Timestamp timestamp;
    private int agentId;

    public BirdHouseAgentHeartbeat(int agentId) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.agentId = agentId;
    }
}
