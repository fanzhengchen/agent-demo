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

    private static final AnalysisByteCodeUtil mByteCodeUtil = new AnalysisByteCodeUtil();

    @Parameter
    private String rootDir;

    @Parameter
    private String pattern;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("=========");
        getLog().info("method trace");

        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws Exception {
        if (rootDir == null) {
            rootDir = mByteCodeUtil.getProjectRootDir();
        }
        pattern = System.getProperties().getProperty("analysis.pattern");

        getLog().info("rootDir: " + rootDir + " methodPattern:" + pattern);
        if (pattern == null) {
            getLog().error("method pattern is null");
            return;
        }

        getLog().info("trace begin");
        mByteCodeUtil.analysis(rootDir, pattern);
        getLog().info("trace end");
    }

    public static void main(String[] args)throws Exception{
        mByteCodeUtil.analysis("/Users/fanzhengchen/RenderGroup/agent-demo/spring-boot/",
                "com/qunhe/instdeco/Person.setName:(Ljava/lang/String;)V");
    }
}
