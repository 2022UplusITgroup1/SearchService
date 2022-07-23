package com.uplus.searchservice.service;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import com.uplus.searchservice.controller.message.StatusMessage;
import com.uplus.searchservice.entity.Dictionary;
import com.uplus.searchservice.exception.NoAvailableItemException;
import com.uplus.searchservice.repository.DictionaryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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

    @Deprecated
    public String getCorrectWordInDictionary(String keyword) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = REDIS_PREFIX_KEY + "::" + keyword;

        return valueOperations.get(key);
    }

    /**
     * 사전(dictionary)에 검색가능하도록 변환 - JSH
     */
    // char 유형에 맞는 type return
    public static int findType(int ascii) {
        if(ascii >= 44032) {
            // 한글 단어일 경우 - 1
            return 1;
        } else if(ascii > 47 && ascii < 58) {
            // 숫자일 경우 - 2
            return 2;
        } else if((ascii > 64 && ascii < 91) || (ascii > 96 && ascii < 123)) {
            // 영단어일 경우 (대문자 & 소문자) - 3
            return 3;
        } else if(ascii > 12592) {
            // 한글 자음일 경우 - 4
            return 4;
        } else {
            // 그 외 문자
            return 0;
        }
    }
    public List<String> getConvertWord(String keyword) {
        // 한글 자음 - 영어 해시맵
        HashMap<String, String> consonants = new HashMap<String, String>();
        consonants.put("ㄱ", "R");
        consonants.put("ㄲ", "R");
        consonants.put("ㄴ", "S");
        consonants.put("ㄷ", "E");
        consonants.put("ㄸ", "E");
        consonants.put("ㄹ", "F");
        consonants.put("ㅁ", "A");
        consonants.put("ㅂ", "Q");
        consonants.put("ㅃ", "Q");
        consonants.put("ㅅ", "T");
        consonants.put("ㅈ", "W");
        consonants.put("ㅉ", "W");
        consonants.put("ㅊ", "C");
        consonants.put("ㅋ", "Z");
        consonants.put("ㅌ", "X");
        consonants.put("ㅍ", "B");
        consonants.put("ㅎ", "G");

        // 변환 결과 저장 List
        List ans = new ArrayList();

        int prev = 0;
        for(int i=0;i<keyword.length();i++) {
            char ch = keyword.charAt(i);
            int ascii = (int)ch;

            // 공백은 continue
            if(ascii == 32) continue;

            // 이전과 다른 유형의 char 일 경우, 공백 추가 (맨 처음은 제외)
            if(prev != 0 && findType(ascii) != 0 && prev != findType(ascii)) ans.add(" ");
            prev = findType(ascii);

            // ㄱ(12593) ~ ㅎ(12622)
            if(ascii > 12592 && ascii < 12623) {
                // 한글 자음일 경우 Hashmap 값 가져오기
                ans.add(consonants.get(Character.toString(ch)));
            } else {
                ans.add(ch);
            }
        }

        // List To String
        StringBuilder sb = new StringBuilder(keyword.length());
        for (int i=0;i<ans.size();i++) {
            sb.append(ans.get(i));
        }
        String res = sb.toString();

        // String Split -> List
        List<String> convertWordList = Arrays.asList(res.split(" "));

        return convertWordList;
    }

    /**
     * 데이터베이스에서 올바른 단어 검색 - 결과가 null이 아닐 경우 redis에 캐싱
     */
    @Cacheable(value = "dictionary", key = "#keyword", unless="#result == null")
    public Dictionary getCorrectWordInDB(String keyword) {
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
    @Deprecated
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
}
