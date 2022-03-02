#!/bin/bash
current=$(pwd)
genFolder=$current/../../gen/gradle-templates/springboot
rm $genFolder -rf
mkdir $genFolder -p

./gradlew dependencies --build-file template_build.gradle --configuration testRuntimeClasspath >> $genFolder/test_runtime_classpath_dependencies.txt


