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
    public void init() throws Exception {
        config(new CameraSettings());
    }

    public void takePicture(PictureCaptureHandler pictureCaptureHandler) throws CaptureFailedException {
        camera.takePicture(pictureCaptureHandler);
    }

    public void config(CameraSettings cameraSettings) throws Exception {
        if (camera!=null) {
            camera.close();
        }

        CameraConfiguration config = cameraConfiguration()
                .width(2592)
                .height(1944)
                .automaticWhiteBalance(AutomaticWhiteBalanceMode.AUTO)
                .encoding(Encoding.JPEG)
                .quality(100);

        if (cameraSettings.getBrightness()!=null) {
            config.brightness(cameraSettings.getBrightness());
        }

        if (cameraSettings.getAutomaticWhiteBalanceMode()!=null) {
            config.automaticWhiteBalance(cameraSettings.getAutomaticWhiteBalanceMode());
        }

        if (cameraSettings.getAutomaticWhiteBalanceRedGain()!=null) {
            config.automaticWhiteBalanceRedGain(cameraSettings.getAutomaticWhiteBalanceRedGain());
        }

        if (cameraSettings.getAutomaticWhiteBalanceBlueGain()!=null) {
            config.automaticWhiteBalanceRedGain(cameraSettings.getAutomaticWhiteBalanceBlueGain());
        }

        if (cameraSettings.getContrast()!=null) {
            config.contrast(cameraSettings.getContrast());
        }

        if (cameraSettings.getCrop() != null) {
            config.crop(
                    (float)cameraSettings.getCrop().getX(),
                    (float)cameraSettings.getCrop().getY(),
                    (float)cameraSettings.getCrop().getWidth(),
                    (float)cameraSettings.getCrop().getHeight()
            );
        }

        if (cameraSettings.getDynamicRangeCompressionStrength()!=null) {
            config.dynamicRangeCompressionStrength(cameraSettings.getDynamicRangeCompressionStrength());
        }

        if (cameraSettings.getExposureCompensation()!=null) {
            config.exposureCompensation(cameraSettings.getExposureCompensation());
        }

        if (cameraSettings.getExposureMeteringMode()!=null) {
            config.exposureMeteringMode(cameraSettings.getExposureMeteringMode());
        }

        if (cameraSettings.getExposureMode()!=null) {
            config.exposureMode(cameraSettings.getExposureMode());
        }

        if (cameraSettings.getWidth()!=null) {
            config.width(cameraSettings.getWidth());
        }

        if (cameraSettings.getHeight()!=null) {
            config.height(cameraSettings.getHeight());
        }

        if (cameraSettings.getQuality()!=null) {
            config.quality(cameraSettings.getQuality());
        }

        if (cameraSettings.getIso()!=null) {
            config.iso(cameraSettings.getIso());
        }

        if (cameraSettings.getRotation()!=null) {
            config.rotation(cameraSettings.getRotation());
        }

        if (cameraSettings.getSaturation()!=null) {
            config.saturation(cameraSettings.getSaturation());
        }

        if (cameraSettings.getSharpness()!=null) {
            config.sharpness(cameraSettings.getSharpness());
        }

        if (cameraSettings.getShutterSpeed()!=null) {
            config.shutterSpeed(cameraSettings.getShutterSpeed());
        }

        if (cameraSettings.getMirror()!=null) {
            config.mirror(cameraSettings.getMirror());
        }

        if (cameraSettings.getVideoStabilisation()!=null) {
            config.vdieoStabilsation(cameraSettings.getVideoStabilisation());
        }

        camera = new Camera(config);
    }
}
