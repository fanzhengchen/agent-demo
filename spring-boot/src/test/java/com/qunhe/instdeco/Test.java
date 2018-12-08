/*
 * Test.java
 * Copyright 2018 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.qunhe.instdeco;

/**
 * @author shengxun
 */
public class Test {

    @org.junit.Test
    public void regexRegex() {
        String src = "com/qunhe/instdeco";
        String ret = String.join(".", src.split("[\\W]"));
        System.out.println(ret);
    }
}
