package com.uplus.searchservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.searchservice.dto.TypoCorrectResponseDto;
import com.uplus.searchservice.service.TypoCorrectService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/search")
@RestController
public class SearchController {
    private final TypoCorrectService typoCorrectService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @GetMapping
    public TypoCorrectResponseDto getProductBySearch (@RequestParam("query") String query) {

        TypoCorrectResponseDto typoCorrectResponseDto =typoCorrectService.getCorrectString(query);
        
        logger.info("correct query : "+typoCorrectResponseDto.toString());

        return typoCorrectResponseDto;
    }
}
