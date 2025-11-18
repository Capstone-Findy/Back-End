package com.example.findy._testutils.fixture;

import com.example.findy.api.user.dto.request.UpdateItem;
import com.example.findy.api.user.dto.response.UserRes;

public class UserFixture {
    public static UpdateItem updateItem() {
        return new UpdateItem(1, 1, 1);
    }

    public static UserRes userRes(){
        return new UserRes("name", 1, 1, 1, 1, 1);
    }
}
