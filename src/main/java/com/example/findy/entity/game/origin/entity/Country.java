package com.example.findy.entity.game.origin.entity;

import com.findy.processor.RestDocs;

@RestDocs("국가")
public enum Country {
    @RestDocs("대한민국")
    Korea,
    @RestDocs("중국")
    China,
    @RestDocs("프랑스")
    France,
    @RestDocs("일본")
    Japan,
    @RestDocs("미국")
    USA
}
