package com.uplus.searchservice.controller;

import java.util.ArrayList;
import java.util.List;

import com.uplus.searchservice.dto.response.TypoCorrectResponseDto;
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
         * case 1. 내부 알고리즘으로 파싱한다. (띄어쓰기, 한<-> 변환 )
         * case 2. 오타변환 API를 통해 오타수정된 결과를 받는다 -> product service 상품 검색 결과를 받는다.
         * case 3. DB검색 - redis에 cache됨
         * DB 검색으로 결과가 조회된 경우 -> product service에 상품 검색
         *
         */
        if (query.isEmpty())
            return ResponseMessage.res(StatusCode.NO_CONTENT, StatusMessage.NOT_FOUND_SEARCH_PRODUCT);

        query = query.trim();

        /**
         * 한글/ 영어 혼합인 경우를 고려하여 내부 알고리즘 호출
         */
        List<String> convertedWords = searchService.getConvertWord(query);
        String keyword = String.join(" ", convertedWords);
        logger.debug("search query[" + query + "] -> keyword [" + keyword + "]");

        /**
         * 오타변환 api 를 통해 오타를 수정한다.
         */
        TypoCorrectResponseDto typoCorrectResponseDto = searchService.getTypoCorrectString(keyword);
        if (!typoCorrectResponseDto.getQuery().isEmpty()) {
            logger.debug("Typo correct result : " + typoCorrectResponseDto.getQuery());
            List<PhoneDto> searchPhoneList = searchService.getSearchList(typoCorrectResponseDto.getQuery());
            if (!searchPhoneList.isEmpty())
                return ResponseMessage.res(StatusCode.OK, StatusMessage.SUCCESS_FOUND_SEARCH_PRODUCT, searchPhoneList);
        }

        List<String> keywordList = convertedWords;
        int idx = 0;
        for(String converts : convertedWords){
            //단어 길이 2이상일때만 검색
            if(converts.length() > 1){
                Dictionary dictionaryWord = searchService.getCorrectWordInDB(converts.toUpperCase());
                if (dictionaryWord != null)
                    keywordList.set(idx, dictionaryWord.getCorrectWord());
            }

            idx++;
        }

        keyword = String.join(" ", keywordList);
        logger.debug("search in db query[" + query + "] -> keyword [" + keyword + "]");

        List<PhoneDto> searchPhoneList = searchService.getSearchList(keyword);
        if (searchPhoneList.isEmpty())
            return ResponseMessage.res(StatusCode.NO_CONTENT, StatusMessage.NOT_FOUND_SEARCH_PRODUCT);

        return ResponseMessage.res(StatusCode.OK, StatusMessage.SUCCESS_FOUND_SEARCH_PRODUCT, searchPhoneList);
    }
}
