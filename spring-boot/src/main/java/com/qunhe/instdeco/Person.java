/*
 * Person.java
 * Copyright 2018 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.qunhe.instdeco;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shengxun
 */
@Data
@Accessors(prefix = "m")
public class Person {

    private String mName;
    private Integer mAge;

}
