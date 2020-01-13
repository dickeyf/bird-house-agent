package com.brewingmadscientists.birdhouseagent.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@RequiredArgsConstructor
public class Picture {
    private Timestamp timestamp;
    private int agentId;
    private byte[] picture;

    public Picture(byte[] picture, int agentId) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.picture = picture;
    }
}
