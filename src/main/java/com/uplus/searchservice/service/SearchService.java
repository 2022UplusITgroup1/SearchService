package com.uplus.searchservice.service;

import java.util.List;

import com.uplus.searchservice.controller.message.StatusMessage;
import com.uplus.searchservice.entity.Dictionary;
import com.uplus.searchservice.exception.NoAvailableItemException;
import com.uplus.searchservice.repository.DictionaryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.uplus.searchservice.dto.PhoneDto;
import com.uplus.searchservice.dto.response.SearchResponseDto;
import com.uplus.searchservice.feignclient.ProductServiceClient;

import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class SearchService {

    @Value("${prefix.key}")
    private String REDIS_PREFIX_KEY;

    private final ProductServiceClient productServiceClient;
    private final DictionaryRepository dictionaryRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 사전(dictionary)에 오타 수정 된 케이스가 있는지 확인
     */
    public String getCorrectWordInDictionary(String keyword) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = REDIS_PREFIX_KEY + "::" + keyword;

        return valueOperations.get(key);
    }

    /**
     * 데이터베이스에서 올바른 단어 검색
     */
    public Dictionary getCorrentWordInDB(String keyword) {
        return dictionaryRepository.findByWrongWord(keyword).orElse(null);
    }

    /**
     * productservice에서 상품 리스트 검색 결과 받아오기
     */
    public List<PhoneDto> getSearchList(String keyword) {
        SearchResponseDto searchResponse = productServiceClient.getSearchProducts(keyword);
        if (searchResponse.getStatus() != 200)
            throw new NoAvailableItemException("검색 결과 상품이 존재하지 않습니다");

        List<PhoneDto> phoneList = searchResponse.getData();
        return phoneList;
    }

    /**
     * 오타 수정 된 검색어 데이터베이스에 추가하기
     */
    @Transactional
    public Dictionary updateWordDictionary(Dictionary dictionary) {
        Dictionary savedDictionary = null;

        try {
            savedDictionary = dictionaryRepository.save(dictionary);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(StatusMessage.DB_UPDATE_IS_ZERO);
        }
        return savedDictionary;
    }
    /**
     * 오타 수정 알고리즘
     */
}
