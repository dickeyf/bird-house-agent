package com.brewingmadscientists.birdhouseagent.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@RequiredArgsConstructor
public class BirdHouseAgentHeartbeatDTO {
    private Timestamp timestamp;
    private int agentId;

    public BirdHouseAgentHeartbeatDTO(int agentId) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.agentId = agentId;
    }
}
