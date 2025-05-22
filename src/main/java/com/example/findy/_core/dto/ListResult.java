package com.example.findy._core.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResult<T> extends CommonResult {
    private final List<T> data;

    public ListResult(String message, List<T> data) {
        super(false, message);
        this.data = data;
    }
}
