/*
 * Agent.java
 * Copyright 2018 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.qunhe.instdeco;

import java.lang.instrument.Instrumentation;

/**
 * @author shengxun
 */
public class Agent {

    public static void agentmain(String args, Instrumentation inst) {
        premain(args, inst);
    }

    public static void premain(String agentOps, Instrumentation inst) {
        inst.addTransformer(new Transformer());
    }
}
