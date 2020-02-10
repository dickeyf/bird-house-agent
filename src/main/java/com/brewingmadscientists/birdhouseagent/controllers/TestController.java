package com.brewingmadscientists.birdhouseagent.controllers;

import com.brewingmadscientists.birdhouseagent.dto.CameraSettingsDTO;
import com.brewingmadscientists.birdhouseagent.services.CameraService;
import com.brewingmadscientists.birdhouseagent.streams.sources.PictureSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    CameraService cameraService;

    @Autowired
    public TestController(CameraService cameraService, PictureSource pictureSource) {
        this.cameraService = cameraService;
    }

    @PostMapping("/previewConfig")
    public void config(@RequestBody CameraSettingsDTO cameraSettings) throws Exception {
        cameraService.previewConfig(cameraSettings);
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
