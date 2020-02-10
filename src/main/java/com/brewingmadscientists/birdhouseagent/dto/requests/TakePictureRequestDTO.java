package com.brewingmadscientists.birdhouseagent.dto.requests;

import com.brewingmadscientists.birdhouseagent.dto.CameraSettingsDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;

@Data
@RequiredArgsConstructor
public class TakePictureRequestDTO {
    static ObjectMapper objectMapper = new ObjectMapper();

    static public TakePictureRequestDTO build(RequestDTO requestDTO) throws JsonProcessingException {
        return objectMapper.convertValue(requestDTO.getRequest(), TakePictureRequestDTO.class);
    }

    //This field is null when picture is to be taken with default settings.
    CameraSettingsDTO settings;
}
