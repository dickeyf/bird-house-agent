package com.brewingmadscientists.birdhouseagent.services;

import com.brewingmadscientists.birdhouseagent.dto.CameraSettingsDTO;
import com.brewingmadscientists.birdhouseagent.streams.sources.PictureSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import uk.co.caprica.picam.Camera;
import uk.co.caprica.picam.CameraConfiguration;
import uk.co.caprica.picam.CaptureFailedException;
import uk.co.caprica.picam.PictureCaptureHandler;
import uk.co.caprica.picam.enums.AutomaticWhiteBalanceMode;
import uk.co.caprica.picam.enums.Encoding;
import uk.co.caprica.picam.enums.ExposureMeteringMode;
import uk.co.caprica.picam.enums.ExposureMode;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.ByteArrayOutputStream;

import static uk.co.caprica.picam.CameraConfiguration.cameraConfiguration;

@Service
public class CameraService implements Runnable {
    private Thread thread;
    private volatile boolean exitSignal;

    class PictureHandler implements PictureCaptureHandler {

        private ByteArrayOutputStream out;

        @Override
        public void begin() {
            out = new ByteArrayOutputStream();
        }

        @Override
        public void pictureData(byte[] data) throws Exception {
            out.write(data);
        }

        @Override
        public void end() {
            storePicture(lastPicture);
            pictureSource.sendPicture(result());
        }

        @Override
        public byte[] result() {
            return out.toByteArray();
        }
    }

    class PreviewHandler implements PictureCaptureHandler {

        private ByteArrayOutputStream out;

        @Override
        public void begin() {
            out = new ByteArrayOutputStream();
        }

        @Override
        public void pictureData(byte[] data) throws Exception {
            out.write(data);
        }

        @Override
        public void end() {
            pictureSource.sendPreview(result());
        }

        @Override
        public byte[] result() {
            return out.toByteArray();
        }
    }

    Camera camera = null;
    byte[] lastPicture = null;
    PictureHandler pictureHandler = new PictureHandler();
    PreviewHandler previewHandler = new PreviewHandler();
    PictureSource pictureSource;
    CameraConfiguration lastPreviewConfig=null;

    @Autowired
    CameraService(PictureSource pictureSource) {
        this.pictureSource = pictureSource;
    }

    private synchronized void storePicture(byte[] picture) {
        lastPicture = picture;
    }

    @PostConstruct
    public synchronized void init() throws Exception {
        previewConfig(new CameraSettingsDTO());
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        exitSignal = true;
        thread.join();
    }

    @EventListener(ApplicationReadyEvent.class)
    public synchronized void start() throws CaptureFailedException {
        exitSignal = false;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        int errorCount = 0;
        while (!exitSignal) {
            try {
                takePreviewPicture();
            } catch (CaptureFailedException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    return;
                }
                if (++errorCount>10) {
                    return;
                }
            }
        }
    }

    public synchronized void takePreviewPicture() throws CaptureFailedException {
        camera.takePicture(previewHandler);
    }

    //Take a high quality picture
    public synchronized void takePicture(CameraSettingsDTO settingsDTO) throws Exception {
        CameraConfiguration config = cameraConfiguration()
                .width(2592)
                .height(1944)
                .automaticWhiteBalance(AutomaticWhiteBalanceMode.AUTO)
                .exposureMode(ExposureMode.AUTO)
                //.exposureMeteringMode(ExposureMeteringMode.MATRIX)
                .encoding(Encoding.JPEG)
                .quality(100);

        if (settingsDTO!=null) {
            config = applySettings(config, settingsDTO);
        }

        if (camera!=null) {
            camera.close();
        }

        camera = new Camera(config);
        camera.takePicture(pictureHandler);
        camera.close();
        camera = new Camera(lastPreviewConfig);
    }

    public synchronized byte[] getLastPicture() {
        if (lastPicture!=null) {
            byte[] lastPictureCopy = lastPicture.clone();
            return lastPictureCopy;
        } else {
            return null;
        }
    }

    private CameraConfiguration applySettings(CameraConfiguration config, CameraSettingsDTO cameraSettings) {
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

        return config;
    }

    public synchronized void previewConfig(CameraSettingsDTO cameraSettings) throws Exception {
        if (camera!=null) {
            camera.close();
        }

        CameraConfiguration previewConfig = cameraConfiguration()
                .width(640)
                .height(480)
                .automaticWhiteBalance(AutomaticWhiteBalanceMode.AUTO)
                .encoding(Encoding.JPEG)
                .captureTimeout(2000)
                .quality(100);

        previewConfig = applySettings(previewConfig, cameraSettings);

        try {
            camera = new Camera(previewConfig);
        } catch (Exception e) {
            if (lastPreviewConfig==null) {
                throw e;
            } else {
                //Recover the camera using last known safe settings
                camera = new Camera(lastPreviewConfig);
            }
        }

        lastPreviewConfig = previewConfig;
    }
}
