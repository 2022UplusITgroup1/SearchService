package com.uplus.searchservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class StorageDto {
    
    @JsonProperty("capability")
    private int capability;
}
