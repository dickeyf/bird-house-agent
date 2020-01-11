package com.brewingmadscientists.birdhouseagent.services;

import com.brewingmadscientists.birdhouseagent.dto.CameraSettings;
import org.springframework.stereotype.Service;
import uk.co.caprica.picam.Camera;
import uk.co.caprica.picam.CameraConfiguration;
import uk.co.caprica.picam.CaptureFailedException;
import uk.co.caprica.picam.PictureCaptureHandler;
import uk.co.caprica.picam.enums.AutomaticWhiteBalanceMode;
import uk.co.caprica.picam.enums.Encoding;

import javax.annotation.PostConstruct;

import static uk.co.caprica.picam.CameraConfiguration.cameraConfiguration;

@Service
public class CameraService {
    Camera camera = null;

    @PostConstruct
    public void init() {
        CameraConfiguration config = cameraConfiguration()
                .width(2592)
                .height(1944)
                .automaticWhiteBalance(AutomaticWhiteBalanceMode.AUTO)
                .encoding(Encoding.JPEG)
                .quality(100);

        camera = new Camera(config);
    }

    public void takePicture(PictureCaptureHandler pictureCaptureHandler) throws CaptureFailedException {
        camera.takePicture(pictureCaptureHandler);
    }

    public void config(CameraSettings cameraSettings) {
    }
}
