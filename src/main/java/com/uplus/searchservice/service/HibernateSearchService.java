package com.uplus.searchservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;


import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;

import com.uplus.searchservice.entity.WordDictionary;

@Service
public class HibernateSearchService {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @PostConstruct
    public void buildIndex() throws InterruptedException{
        FullTextEntityManager fullTextEntityManager=Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();

        logger.info("HibernateSearchService buildIndex! ");
    }

    // public HibernateSearchService() throws InterruptedException{
    //     buildIndex();
    // }

    @SuppressWarnings("unchecked")
    public List<WordDictionary> searchDictionary(String keyword){

        logger.info("fullTextEntityManager before");
        FullTextEntityManager fullTextEntityManager=Search.getFullTextEntityManager(entityManager);// 에러

        logger.info("fullTextEntityManager after");

        QueryBuilder queryBuilder= fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(WordDictionary.class).get();
        
        //dsl
        Query query = queryBuilder.keyword().wildcard().onField("wrongWord").matching(keyword).createQuery();

        
        logger.info("queryBuilder query : "+query.toString());
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, WordDictionary.class);

        logger.info("call searchDictionary keyword : "+keyword+" "+fullTextQuery.toString());

        return (List<WordDictionary>) fullTextQuery.getResultList();

    }

}
