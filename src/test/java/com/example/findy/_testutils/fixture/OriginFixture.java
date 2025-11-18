package com.example.findy._testutils.fixture;

import com.example.findy.api.game.origin.dto.request.ResultReq;
import com.example.findy.api.game.origin.dto.response.ResultRes;

import java.util.ArrayList;
import java.util.List;

public class OriginFixture {
    public static ResultReq resultReq() {
        return new ResultReq(1, 1, 1, 1, 1, 1);
    }

    public static List<ResultRes> resultResList() {
        List<ResultRes> resultResList = new ArrayList<>();
        resultResList.add(new ResultRes(1, 1));
        return resultResList;
    }
}
