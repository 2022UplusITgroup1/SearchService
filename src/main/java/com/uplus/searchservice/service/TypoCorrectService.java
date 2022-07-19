package com.uplus.searchservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.uplus.searchservice.dto.TypoCorrectResponseDto;
import com.uplus.searchservice.feignclient.TypoCorrectClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TypoCorrectService {

    private final TypoCorrectClient typoCorrectClient;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TypoCorrectResponseDto getTypoCorrectString(String query){
        TypoCorrectResponseDto typoCorrectResponseDto =typoCorrectClient.getCorrectString(query);


        return typoCorrectResponseDto;
    }

    public String getSearchString(String query){
        String searchString=query;



        return searchString;
    }

}
