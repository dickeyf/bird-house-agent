package com.brewingmadscientists.birdhouseagent.streams.sinks;

import com.brewingmadscientists.birdhouseagent.dto.CameraSettingsDTO;
import com.brewingmadscientists.birdhouseagent.dto.requests.RequestDTO;
import com.brewingmadscientists.birdhouseagent.dto.requests.TakePictureRequestDTO;
import com.brewingmadscientists.birdhouseagent.services.CameraService;
import com.brewingmadscientists.birdhouseagent.streams.BirdHouseChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import uk.co.caprica.picam.CaptureFailedException;

@Component
@EnableBinding(BirdHouseChannel.class)
public class RequestSink {
    @Autowired
    private CameraService cameraService;

    @StreamListener(BirdHouseChannel.REQUESTS)
    public void requestSink(RequestDTO requestDTO) throws Exception {
        //TODO: Catch errors here, and report them via an error topic
        switch (requestDTO.getRequestType()) {
            case TakePicture:
                handleTakePictureRequest(requestDTO);
                break;
        }
    }

    private void handleTakePictureRequest(RequestDTO requestDTO) throws Exception {
        //TODO: Build this from the RequestDTO once I find a way to make JsonAnySetter work.
        //TakePictureRequestDTO takePictureRequestDTO = new TakePictureRequestDTO();
        //CameraSettingsDTO settingsDTO = takePictureRequestDTO.getSettings();
        cameraService.takePicture(null);
    }
}
