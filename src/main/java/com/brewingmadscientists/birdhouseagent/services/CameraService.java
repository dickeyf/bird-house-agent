package com.brewingmadscientists.birdhouseagent.services;

import com.brewingmadscientists.birdhouseagent.dto.CameraSettings;
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
            lastPicture = out.toByteArray();
            storePicture(lastPicture);
            pictureSource.sendPicture(getLastPicture());
        }

        @Override
        public byte[] result() {
            return out.toByteArray();
        }
    }

    Camera camera = null;
    byte[] lastPicture = null;
    PictureHandler pictureHandler = new PictureHandler();
    PictureSource pictureSource;

    @Autowired
    CameraService(PictureSource pictureSource) {
        this.pictureSource = pictureSource;
    }

    private synchronized void storePicture(byte[] picture) {
        lastPicture = picture;
    }

    @PostConstruct
    public synchronized void init() throws Exception {
        config(new CameraSettings());
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
                takePicture(pictureHandler);
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

    public synchronized void takePicture(PictureCaptureHandler pictureCaptureHandler) throws CaptureFailedException {
        camera.takePicture(pictureCaptureHandler);
    }

    public synchronized byte[] getLastPicture() {
        byte[] lastPictureCopy = lastPicture.clone();
        return lastPictureCopy;
    }

    public synchronized void config(CameraSettings cameraSettings) throws Exception {
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
