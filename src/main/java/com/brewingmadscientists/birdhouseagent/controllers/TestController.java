package com.brewingmadscientists.birdhouseagent.controllers;

import com.brewingmadscientists.birdhouseagent.dto.CameraSettings;
import com.brewingmadscientists.birdhouseagent.services.CameraService;
import com.brewingmadscientists.birdhouseagent.streams.sources.PictureSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import uk.co.caprica.picam.CaptureFailedException;
import uk.co.caprica.picam.PictureCaptureHandler;

import java.io.ByteArrayOutputStream;

@RestController
public class TestController {
    CameraService cameraService;

    @Autowired
    public TestController(CameraService cameraService, PictureSource pictureSource) {
        this.cameraService = cameraService;
    }

    @PostMapping("/config")
    public void config(@RequestBody CameraSettings cameraSettings) throws Exception {
        cameraService.config(cameraSettings);
    }

    @GetMapping(
            value="/getPicture",
            produces= MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public byte[] getPicture() {
        return cameraService.getLastPicture();
    }
}
