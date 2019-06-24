package com.test.controller;

import com.test.pojo.User;
import com.test.service.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by 任春鹏 on 2019/5/18.
 */
@Controller
public class TestController {

    @Autowired
    private Index index;

    @RequestMapping("index")
    @ResponseBody
    public String index(@Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            return bindingResult.getFieldError().getDefaultMessage();
        }
        return user.getUserName();
//        return index.login();
    }
}
