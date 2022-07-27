package com.uplus.searchservice.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uplus.searchservice.dto.PhoneDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SearchResultDto {
    List<PhoneDto> searchResults;
    String correctWord;
}
