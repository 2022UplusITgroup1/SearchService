package com.uplus.searchservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import lombok.Getter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="dictionary")
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Integer id;

    @NotNull
    @Column(name = "wrong_word")
    private String wrongWord;

    @NotNull
    @Column(name = "correct_Word")
    private String correctWord;


}
