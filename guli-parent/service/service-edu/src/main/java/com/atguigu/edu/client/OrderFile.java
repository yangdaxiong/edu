package com.atguigu.edu.client;

import org.springframework.stereotype.Component;

@Component
public class OrderFile implements OrderClient {


    @Override
    public Boolean isBuyCourse(String memberid, String id) {
        return false;
    }
}
