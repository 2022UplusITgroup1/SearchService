package com.uplus.searchservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class TypoCorrectResponseDto {

    @JsonProperty("errata")
    private String query;
    
}
