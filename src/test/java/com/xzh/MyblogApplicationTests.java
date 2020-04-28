package com.xzh;

import com.xzh.service.impl.TypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyblogApplicationTests {

    @Autowired
    TypeServiceImpl typeService;
    void contextLoads() {
        System.out.println(typeService.getTypeByName("aa"));

    }

}
