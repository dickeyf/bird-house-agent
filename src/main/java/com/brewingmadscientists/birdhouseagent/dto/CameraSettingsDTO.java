package com.brewingmadscientists.birdhouseagent.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import uk.co.caprica.picam.enums.*;

@RequiredArgsConstructor
@Data
public class CameraSettingsDTO {
    private Integer width;
    private Integer height;
    private Encoding encoding;
    private Integer quality;
    private StereoscopicMode stereoscopicMode;
    private Boolean swapEyes;
    private Integer brightness;
    private Integer contrast;
    private Integer saturation;
    private Integer sharpness;
    private Boolean videoStabilisation;
    private Integer shutterSpeed;
    private Integer iso;
    private ExposureMode exposureMode;
    private ExposureMeteringMode exposureMeteringMode;
    private Integer exposureCompensation;
    private DynamicRangeCompressionStrength dynamicRangeCompressionStrength;
    private AutomaticWhiteBalanceMode automaticWhiteBalanceMode;
    private Float automaticWhiteBalanceRedGain;
    private Float automaticWhiteBalanceBlueGain;
    private ImageEffect imageEffect;
    private Mirror mirror;
    private Integer rotation;
    private java.awt.geom.Rectangle2D.Float crop;
}
