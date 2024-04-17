/*
 * Copyright 2022 Albert Tregnaghi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package de.jcup.dependagen.springboot;

import static de.jcup.dependagen.util.OutputConstants.LINE;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.jcup.dependagen.Console;
import de.jcup.dependagen.VersionSupport;
import de.jcup.dependagen.gradle.GradleDependencyTreeOutputParser;
import de.jcup.dependagen.model.DependaGenModel;
import de.jcup.dependagen.util.TextFileReader;
import de.jcup.dependagen.util.TextFileWriter;

@Component
public class SpringBootLibrariesGradleFileGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(SpringBootLibrariesGradleFileGenerator.class);

    @Autowired
    VersionSupport versionSupport;

    @Autowired
    GradleDependencyTreeOutputParser parser;

    @Autowired
    SpringBootGradleLibrariesSourceFileContentFactory factory;

    @Autowired
    TextFileWriter writer;

    @Autowired
    TextFileReader reader;

    public void generate() throws IOException {

        /* generate dependency output */
        File directory = new File("./gradle-templates/spring-boot");
        File file = new File(directory, "generate-gradle-output.sh");
        LOG.debug("Start gradle process by executing \nscript:{} \ninside:{}", file, directory);

        ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath());
        pb.directory(directory);
        Process process = pb.start();
        try {
            process.waitFor(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new IOException("Was not able to wait for result", e);
        }
        String testRuntimeClasspathTree = reader.read(new File("./gen/gradle-templates/springboot/test_runtime_classpath_dependencies.txt"));

        DependaGenModel model = parser.parseDependencyTreeText(testRuntimeClasspathTree);
        Console.LOG.info("Model parsed");
        String created = factory.create(model);

        File targetFile = new File("./gen/gradle-templates/springboot/spring_boot_dependagen.gradle");
        writer.write(targetFile, created, true);

        Console.LOG.info(created);
        Console.LOG.info(LINE);
        Console.LOG.info(("wrote to " + targetFile.getAbsolutePath()));
        Console.LOG.info(LINE);

    }

}
