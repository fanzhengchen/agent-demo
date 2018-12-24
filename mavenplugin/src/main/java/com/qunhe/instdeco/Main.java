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

/**
 * @author shengxun
 */
@Mojo(name = "analysis")
public class Main extends AbstractMojo {

    String test;

    @Parameter
    private String rootDir;

    @Parameter
    private String pattern;

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
        TraceAnalyser analyser = new TraceAnalyser();
    }

    public static void main(String[] args)throws Exception{
        TraceAnalyser analyser = new TraceAnalyser();
//        analyser.trace("/home/mark/RenderGroup/diyrenderservice", "TraceAnalyser");
        analyser.trace("/home/mark/agent-demo/mavenplugin","sssss");
    }
}
