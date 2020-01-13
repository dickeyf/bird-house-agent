package com.brewingmadscientists.birdhouseagent.streams.sources;

import com.brewingmadscientists.birdhouseagent.dto.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(BirdHouseChannel.class)
public class PictureSource {
    private static final Logger log = LoggerFactory.getLogger(HeartbeatSource.class);

    private static final int agentIdentifier = 1;

    @Autowired
    BirdHouseChannel birdHouseChannel;

    public void sendPicture(byte[] pictureData) {
        Picture picture = new Picture(pictureData, agentIdentifier);

        birdHouseChannel.picture().send(MessageBuilder.withPayload(picture).build());
    }

}
