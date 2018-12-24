/*
 * AnalysisByteCodeUtil.java
 * Copyright 2018 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.qunhe.instdeco;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.LockSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shengxun
 * @date 10/24/18 9:51 PM
 */
public class AnalysisByteCodeUtil {
    private static final Pattern CLASS_PATTERN = Pattern.compile("(?<=class\\s)[\\w.]+");

    private static final Pattern SIGNATURE_PATTERN = Pattern.compile("(?<=descriptor:\\s)\\(\\S+");

    private static final Pattern METHOD_NAME = Pattern.compile("([a-zA-Z$\\d])+(?=\\()");

    private static final Pattern METHOD_INVOKE_PATTERN = Pattern.compile("(?<=Method\\s)\\S+");

    public String getProjectRootDir() throws IOException {
        return exec("pwd");
    }

    private static final Map<String, HashSet<String>> CALL_GRAPH = new HashMap<>();

    private static BufferedReader execute(String cmd) throws IOException {
        Process process = Runtime.getRuntime().exec(cmd);
        InputStream inputStream = process.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private static String exec(String cmd) throws IOException {
        BufferedReader reader = execute(cmd);
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            if (flag) {
                result.append("\n");
            }
            result.append(line);
            flag = true;
        }
        return result.toString();
    }

    private static List<String> getMethodSignature(String name, String content) {
//        String name = className.replace(".", "/");
        List<String> list = Lists.newArrayList();
        Matcher matcher = SIGNATURE_PATTERN.matcher(content);
        while (matcher.find()) {
            list.add(matcher.group());
        }

        matcher = METHOD_NAME.matcher(content);

        List<String> methods = Lists.newArrayList();
        while (matcher.find()) {
            methods.add(matcher.group());
        }

        int size = methods.size();
        List<String> result = Lists.newArrayList();
        for (int i = 0; i < size; ++i) {
            String methodName = methods.get(i);
            String signature = list.get(i);
            result.add(name + "." + methodName + ":" + signature);
        }

        if (list.size() > size) {
            result.add(name + ".static {}:" + list.get(size));
        }
        return result;
    }

    private static void analysisByteCode(String cmd, List<String> methodSignature, String name)
            throws IOException {

        BufferedReader reader = execute(cmd);

        String line = null;
        int index = -1;
        while ((line = reader.readLine()) != null) {
            if (line.matches("^\\s+Code:$")) {
                //System.out.println("##" + line + "###");
                ++index;
            } else {
                Matcher matcher = METHOD_INVOKE_PATTERN.matcher(line);
                while (matcher.find()) {
                    String calledFrom = methodSignature.get(index);
                    String invokeMethod = matcher.group();
                    if (line.contains("invokespecial") && !line.contains("<init>")) {
                        invokeMethod = name + "." + invokeMethod;
                    }
                    System.out.println(invokeMethod);

                    if (!CALL_GRAPH.containsKey(invokeMethod)) {
                        CALL_GRAPH.put(invokeMethod, Sets.newHashSet());
                    }
                    CALL_GRAPH.get(invokeMethod)
                            .add(calledFrom);
                }
            }
        }

    }

    private void print(String methodSignature, int dep) {
        for (int i = 0; i < dep; ++i) {
            System.out.print("  ");
        }
        System.out.println(methodSignature);
        System.out.println();
    }

    private void dfsTrace(String methodSignature, int dep) {
        HashSet<String> called = CALL_GRAPH.get(methodSignature);
        if (called == null) {
            print(methodSignature, dep);
            return;
        }
        for (String call : called) {
            print(methodSignature, dep);
            dfsTrace(call, dep + 1);

        }
    }

    private void trace(String methodSignature) {
        System.out.println("=====================================================");
        dfsTrace(methodSignature, 0);
        System.out.println("=====================================================");
    }


    public void analysis(String rootDir, String tag) throws Exception {
        String buildPath = rootDir + "/target/classes";
        Path path = Paths.get(buildPath);

        Files.walkFileTree(path, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir,
                    final BasicFileAttributes attrs)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                    throws IOException {
                final String fullPath = file.toString();
                final String fileName = file.getFileName().toString();
                if (fileName.endsWith(".class")) {
                    System.out.println("filename " + fileName);
                    String content = exec("javap -s -private " + fullPath);
                    Matcher matcher = CLASS_PATTERN.matcher(content);
                    /**
                     * it must be class rather than interface
                     */
                    if (matcher.find()) {
                        String className = matcher.group();
                        String name = className.replace(".", "/");
                        List<String> signatures = getMethodSignature(name,
                                content);
                        analysisByteCode("javap -c -private " + fullPath, signatures,
                                name);
                    }
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(final Path file, final IOException exc)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
        trace(tag);

    }

}
