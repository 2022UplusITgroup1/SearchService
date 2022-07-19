package com.uplus.searchservice.controller;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uplus.searchservice.dto.TypoCorrectResponseDto;
import com.uplus.searchservice.entity.WordDictionary;
import com.uplus.searchservice.service.HibernateSearchService;
import com.uplus.searchservice.service.TypoCorrectService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/search")
@RestController
public class SearchController {
    private final TypoCorrectService typoCorrectService;
    private final HibernateSearchService hibernateSearchService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public String getProductBySearch (@RequestParam("query") String query) {
        String searchWord="";
        TypoCorrectResponseDto typoCorrectResponseDto =typoCorrectService.getTypoCorrectString(query);
        
        logger.info("correct query : "+typoCorrectResponseDto.toString());
        String correctQuery=typoCorrectResponseDto.getQuery();
        if(correctQuery==""){
            correctQuery=query;
        }
        List<WordDictionary> dictionaryEntityList=hibernateSearchService.searchDictionary(correctQuery);

        if(dictionaryEntityList.size()!=0){//DB사전 에 없을시
            searchWord=typoCorrectService.getSearchString(dictionaryEntityList.get(0).getCorrectWord());
        }else{
            searchWord=query;
        }
        
        

        // logger.info("HibernateSearchService query : "+dictionaryEntity);

        // WordDictionary test=new WordDictionary();

        return searchWord;

        // return dictionaryEntity.get(0);
    }

}
