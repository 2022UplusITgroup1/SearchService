package com.uplus.searchservice.dto.response;

import java.util.List;

import com.uplus.searchservice.dto.PhoneDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SearchResponseDto {

    private int status;
    private String message;
    private List<PhoneDto> data;

}
