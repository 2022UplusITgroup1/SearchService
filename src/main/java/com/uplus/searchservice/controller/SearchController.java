package com.uplus.searchservice.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.searchservice.controller.message.ResponseMessage;
import com.uplus.searchservice.controller.message.StatusCode;
import com.uplus.searchservice.controller.message.StatusMessage;
import com.uplus.searchservice.dto.PhoneDto;
import com.uplus.searchservice.dto.response.TypoCorrectResponseDto;
import com.uplus.searchservice.entity.WordDictionary;
import com.uplus.searchservice.service.HibernateSearchService;
import com.uplus.searchservice.service.SearchService;
import com.uplus.searchservice.service.TypoCorrectService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/search")
@RestController
public class SearchController {
    private final TypoCorrectService typoCorrectService;
    private final SearchService searchService;


    private final HibernateSearchService hibernateSearchService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseMessage getProductBySearch (@RequestParam("query") String query) {
        String searchWord="";
        TypoCorrectResponseDto typoCorrectResponseDto =typoCorrectService.getTypoCorrectString(query);
        
        logger.info("correct query : "+typoCorrectResponseDto.toString());
        String correctQuery=typoCorrectResponseDto.getQuery();
        if(correctQuery==""){
            correctQuery=query;
        }
        List<WordDictionary> dictionaryEntityList=hibernateSearchService.searchDictionary(correctQuery);

        if(dictionaryEntityList.size()!=0){//DB사전 에 있을시
            searchWord=typoCorrectService.getSearchString(dictionaryEntityList.get(0).getCorrectWord());
        }else{
            searchWord=correctQuery;
        }
        
        logger.info("searchWord : "+searchWord);

        List<PhoneDto> searchPhoneList= searchService.getSearchList(searchWord);

        logger.info("searchPhoneList size : "+searchPhoneList.size());

        if (searchPhoneList.isEmpty())
            return ResponseMessage.res(StatusCode.NO_CONTENT, StatusMessage.NOT_FOUND_SEARCH_PRODUCT);

        return ResponseMessage.res(StatusCode.OK, StatusMessage.SUCCESS_FOUND_SEARCH_PRODUCT, searchPhoneList);

    }

}
