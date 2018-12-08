/*
 * AgentClassPool.java
 * Copyright 2018 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.qunhe.instdeco;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

/**
 * @author shengxun
 */
public class AgentClassPool extends ClassPool {

    private ClassLoader mClassLoader;


    public AgentClassPool(ClassLoader classLoader) {
        mClassLoader = classLoader;
    }


    @Override
    public ClassLoader getClassLoader() {
        if (mClassLoader != null) {
            return mClassLoader;
        }
        return super.getClassLoader();
    }

    @Override
    public Class toClass(final CtClass clazz) throws CannotCompileException {
        return super.toClass(clazz);
    }
}
