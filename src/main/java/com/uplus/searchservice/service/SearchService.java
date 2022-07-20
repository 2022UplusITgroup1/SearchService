package com.uplus.searchservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uplus.searchservice.dto.PhoneDto;
import com.uplus.searchservice.dto.response.SearchResponseDto;
import com.uplus.searchservice.feignclient.ProductServiceClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SearchService {
    
    private final ProductServiceClient productServiceClient;

    //searchword로 productservice로 전송해 받아오기

    public List<PhoneDto> getSearchList(String query){
        
        SearchResponseDto searchResponseDto=productServiceClient.getSearchProduct(query);
        List<PhoneDto> phoneList = searchResponseDto.getData();
             
        return phoneList;
    }

}
