package com.brewingmadscientists.birdhouseagent.dto.requests;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class RequestDTO {
    public enum RequestType {
        TakePicture
    }
    private RequestType requestType;
//    @JsonAnySetter
    private Map<String, Object> request = null;
}
