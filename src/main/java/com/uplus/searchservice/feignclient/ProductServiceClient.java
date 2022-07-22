package com.uplus.searchservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uplus.searchservice.dto.response.SearchResponseDto;

@FeignClient("productservice")
public interface ProductServiceClient {
    @GetMapping("/product/search")
    SearchResponseDto getSearchProducts(@RequestParam(value = "word") String keyword);
}
