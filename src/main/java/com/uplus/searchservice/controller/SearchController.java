package com.uplus.searchservice.controller;

import java.util.ArrayList;
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
        logger.info("search query : " + query);

        /**
         * case 1. query 파싱 후
         * case 2. redis cache에서 검색
         * case 3. 검색하지 못한 단어는 내부 오타 수정 알고리즘 적용
         * => 
         * 
         */



        /*
         * query 파싱
         */
        List<String> convertedKeyword=searchService.getConvertWord(query);


        List<String> searchKeywordList=new ArrayList<String>();
        
        for(String str : convertedKeyword){
            //단어 길이 2이상일때만 redis searching
            if(str.length()>1){
                //redis 캐시 searching
                String redisKeyword = searchService.getCorrectWordInDictionary(str);

                if (redisKeyword == null) {

                    /**
                     * 오타 수정 알고리즘 적용
                     */

                    //임시
                    redisKeyword=str;

                }

                searchKeywordList.add(redisKeyword);

            }else{
                searchKeywordList.add(str);
            }

        }


        String searchKeyword = String.join(" ", searchKeywordList);


        // String searchKeyword = searchService.getCorrectWordInDictionary(query);

        // if (searchKeyword == null) {
        //     /**
        //      * 오타 수정 알고리즘 적용
        //      */
        //     // List<String> convertedKeyword=searchService.getConvertWord(query);
        //     // searchKeyword=String.join(" ", convertedKeyword);

        // }

        logger.info("converted keyword : " + searchKeyword);

        logger.info("search keyword : " + searchKeyword);
        List<PhoneDto> searchPhoneList = searchService.getSearchList(searchKeyword);
        if (searchPhoneList.isEmpty())
            return ResponseMessage.res(StatusCode.NO_CONTENT, StatusMessage.NOT_FOUND_SEARCH_PRODUCT);

        if (query != searchKeyword) {
            // 오타 수정된 키워드이기 때문에 데이터베이스에 저장
            Dictionary dictionary = Dictionary.builder().wrongWord(query).correctWord(searchKeyword).build();
            searchService.updateWordDictionary(dictionary);
        }
        return ResponseMessage.res(StatusCode.OK, StatusMessage.SUCCESS_FOUND_SEARCH_PRODUCT, searchPhoneList);
    }
}
