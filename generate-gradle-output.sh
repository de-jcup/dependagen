#!/bin/bash
current=$(pwd)
genFolder=$current/gen
rm $genFolder -rf
mkdir $genFolder
cd gradle-projects/demo

./gradlew dependencies --configuration testRuntimeClasspath >> $genFolder/test_runtime_classpath_dependencies.txt


