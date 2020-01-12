package com.brewingmadscientists.birdhouseagent.controllers;

import com.brewingmadscientists.birdhouseagent.dto.CameraSettings;
import com.brewingmadscientists.birdhouseagent.services.CameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import uk.co.caprica.picam.CaptureFailedException;
import uk.co.caprica.picam.PictureCaptureHandler;

import java.io.ByteArrayOutputStream;

@RestController
public class TestController {
    CameraService cameraService;

    @Autowired
    public TestController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @PostMapping("/config")
    public void config(CameraSettings cameraSettings) throws Exception {
        cameraService.config(cameraSettings);
    }

    @GetMapping(
            value="/getPicture",
            produces= MediaType.IMAGE_JPEG_VALUE
    )
    @ResponseBody
    public DeferredResult<byte[]> getPicture() throws CaptureFailedException {
        DeferredResult<byte[]> result = new DeferredResult<>();

        cameraService.takePicture(new PictureCaptureHandler() {
            private ByteArrayOutputStream out;

            @Override
            public void begin() throws Exception {
                out = new ByteArrayOutputStream();
            }

            @Override
            public void pictureData(byte[] data) throws Exception {
                out.write(data);
            }

            @Override
            public void end() throws Exception {
                result.setResult(result());
            }

            @Override
            public byte[] result() {
                return out.toByteArray();
            }
        });

        return result;
    }
}
