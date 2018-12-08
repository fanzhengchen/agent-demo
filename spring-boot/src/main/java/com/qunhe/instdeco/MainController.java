/*
 * MainController.java
 * Copyright 2018 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.qunhe.instdeco;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shengxun
 */
@RestController
public class MainController {

    @PostMapping("/person")
    public Person getPerson(HttpServletResponse response, @RequestBody Person person) {
        person.setAge(10);
        person.setName("markfan");
        return person;
    }

    @GetMapping("/test")
    public Person person(HttpServletRequest request, HttpServletResponse response) {
        Person person = new Person();
        person.setAge(10);
        person.setName("markfan");
        return person;
    }
}
