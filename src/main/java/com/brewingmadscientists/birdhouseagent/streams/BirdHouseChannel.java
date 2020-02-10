package com.brewingmadscientists.birdhouseagent.streams;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public interface BirdHouseChannel {
    String HEARTBEAT = "heartbeat";
    String PICTURE = "picture";
    String PREVIEWS = "previews";
    String REQUESTS = "requests";

    //Outputs heartbeats periodically
    @Output
    MessageChannel heartbeat();

    //Outputs pictures taken will full quality as a response to the takePicture request
    @Output
    MessageChannel picture();

    //Output pictures taken at preview quality periodically
    @Output
    MessageChannel previews();

    //Input requests from client
    @Input
    MessageChannel requests();
}
