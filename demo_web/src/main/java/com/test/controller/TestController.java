package com.test.controller;

import com.test.service.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 任春鹏 on 2019/5/18.
 */
@Controller
public class TestController {

    @Autowired
    private Index index;

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return index.login();
    }
}
