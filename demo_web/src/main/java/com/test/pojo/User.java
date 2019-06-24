package com.test.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by 任春鹏 on 2019-05-28.
 */
public class User {
    @NotNull
    @Size(min= 3, max = 5, message = "名字只能是3到5位")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
