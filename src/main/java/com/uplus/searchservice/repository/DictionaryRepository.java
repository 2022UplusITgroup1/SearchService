package com.uplus.searchservice.repository;

import com.uplus.searchservice.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DictionaryRepository extends JpaRepository<Dictionary, Integer> {
    Optional<Dictionary> findByWrongWord(String keyword);
}
