package com.brewingmadscientists.birdhouseagent.streams.sources;

import com.brewingmadscientists.birdhouseagent.dto.PictureDTO;
import com.brewingmadscientists.birdhouseagent.streams.BirdHouseChannel;
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

    BirdHouseChannel birdHouseChannel;

    @Autowired
    public PictureSource(BirdHouseChannel birdHouseChannel) {
        this.birdHouseChannel = birdHouseChannel;
    }

    //Sends a picture
    public void sendPicture(byte[] pictureData) {
        PictureDTO picture = new PictureDTO(pictureData, agentIdentifier);

        birdHouseChannel.picture().send(MessageBuilder.withPayload(picture).build());
    }

    //Sends a preview quality picture
    public void sendPreview(byte[] previewData) {
        PictureDTO preview = new PictureDTO(previewData, agentIdentifier);

        birdHouseChannel.previews().send(MessageBuilder.withPayload(preview).build());
    }
}
