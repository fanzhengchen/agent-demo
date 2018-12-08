#!/usr/bin/env sh


export MAVEN_OPTS=-javaagent:agent/lib/agent.jar

mvn --projects spring-boot jetty:run -Djetty.port=12345