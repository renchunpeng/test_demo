package com.test.service;

import org.springframework.stereotype.Service;

/**
 * Created by 任春鹏 on 2019/5/18.
 */
@Service
public class IndexImpl implements Index{

    @Override
    public String login() {
        return "hello java";
    }
}
