/*
 * CommandProcessor.java
 * Copyright 2018 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.qunhe.instdeco;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author shengxun
 */
@Component
public class CommandProcessor implements CommandLineRunner {
    @Override
    public void run(final String... args) throws Exception {
        System.out.println("running");
        Integer integer = new Integer(3);

        FooService fooService = new FooServiceImpl();

        String text = fooService.foo();
        System.out.println(text);
    }
}
