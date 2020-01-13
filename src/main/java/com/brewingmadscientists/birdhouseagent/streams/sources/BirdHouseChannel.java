package com.brewingmadscientists.birdhouseagent.streams.sources;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface BirdHouseChannel {
    String HEARTBEAT = "heartbeat";
    String PICTURE = "picture";

    @Output
    MessageChannel heartbeat();

    @Output
    MessageChannel picture();
}
