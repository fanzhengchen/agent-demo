/*
 * Main.java
 * Copyright 2018 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package com.qunhe.instdeco;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Map;
import java.util.Properties;

/**
 * @author shengxun
 */
@Mojo(name = "analysis")
public class Main extends AbstractMojo {

    String test;

    @Parameter
    private String rootDir;

    @Parameter
    private String classPattern;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("method trace");

        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws Exception {
        Properties properties = System.getProperties();
        String classPattern = properties.getProperty("analysis.classPattern");
        String methodPattern = properties.getProperty("analysis.methodPattern");
        TraceAnalyser analyser = new TraceAnalyser();
        System.out.println("rootDir " + rootDir);
        System.out.println("classPattern " + classPattern);
        System.out.println("methodPattern " + methodPattern);
        analyser.trace(rootDir, classPattern, methodPattern);
    }

    public static void main(String[] args) throws Exception {
        TraceAnalyser analyser = new TraceAnalyser();
        Map<String, String> env = System.getenv();
        String homePath = env.get("HOME");
        String dir = System.getProperty("user.dir");
//        analyser.trace(homePath + "/RenderGroup/diyrenderservice",
//                "DesignSnapshotServiceClient",
//                "getSnapshot");
        analyser.trace(dir +"/spring-boot","x","Y");
    }
}
