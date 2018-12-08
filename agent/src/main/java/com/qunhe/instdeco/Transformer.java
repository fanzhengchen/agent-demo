/*
 * Transformer.java
 * Copyright 2018 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.qunhe.instdeco;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.scopedpool.ScopedClassPool;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.security.ProtectionDomain;

/**
 * @author shengxun
 */
public class Transformer implements ClassFileTransformer {

    private ClassPool mClassPool = null;

    @Override
    public byte[] transform(final ClassLoader loader, final String className,
            final Class<?> classBeingRedefined,
            final ProtectionDomain protectionDomain, final byte[] classfileBuffer)
            throws IllegalClassFormatException {

        String targetClassName = "MainController";
        if (className.endsWith(targetClassName)) {
            try {
                return mockClass(loader, String.join(".", className.split("[\\W]")),
                        classfileBuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }


    private byte[] mockClass(ClassLoader loader, String className, byte[] classfileBuffer)
            throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new LoaderClassPath(loader));
        CtClass targetClass = classPool.getCtClass(className);
        ClassLoader classLoader = targetClass.getClass().getClassLoader();

        targetClass.defrost();
        CtMethod ctMethod = targetClass.getDeclaredMethod("person");
        String packageName = "com.qunhe.instdeco";
        ctMethod.setBody("{" +
                String.format("%s.Person person = new %s.Person();", packageName, packageName) +
                "person.setAge(Integer.valueOf(27));" +
                "person.setName(\"shengxun\");" +
                "return person;" +
                "}");
        targetClass.freeze();
        return targetClass.toBytecode();
    }
}
