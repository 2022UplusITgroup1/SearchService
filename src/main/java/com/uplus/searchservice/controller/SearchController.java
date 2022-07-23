package com.uplus.searchservice.controller;

import java.util.List;

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
import com.uplus.searchservice.entity.Dictionary;
import com.uplus.searchservice.service.SearchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/search")
@RestController
public class SearchController {
    private final SearchService searchService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseMessage getProductBySearch(@RequestParam("query") String query) {
        /**
         * case 1. query 파싱 후
         * case 2. DB검색 - redis에 cache됨
         *
         */

        if (query.isEmpty())
            return ResponseMessage.res(StatusCode.NO_CONTENT, StatusMessage.NOT_FOUND_SEARCH_PRODUCT);
        /*
         * query 파싱
         */
        List<String> convertedWords = searchService.getConvertWord(query);

        List<String> keywordList = convertedWords;
        int idx = 0;
        for(String converts : convertedWords){
            //단어 길이 2이상일때만 검색
            if(converts.length() > 1){
                Dictionary dictionaryWord = searchService.getCorrectWordInDB(converts);
                if (dictionaryWord != null)
                    keywordList.set(idx, dictionaryWord.getCorrectWord());
            }
            idx++;
        }

        String keyword = String.join(" ", keywordList);

        logger.debug("search query[" + query + "] -> keyword [" + keyword + "]");
        List<PhoneDto> searchPhoneList = searchService.getSearchList(keyword);
        if (searchPhoneList.isEmpty())
            return ResponseMessage.res(StatusCode.NO_CONTENT, StatusMessage.NOT_FOUND_SEARCH_PRODUCT);

        return ResponseMessage.res(StatusCode.OK, StatusMessage.SUCCESS_FOUND_SEARCH_PRODUCT, searchPhoneList);
    }
}
