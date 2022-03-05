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
package de.jcup.dependagen.gradle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.jcup.dependagen.model.DependaGenModel;
import de.jcup.dependagen.model.DependencyNode;
import de.jcup.dependagen.model.RootDependencyNode;
import de.jcup.dependagen.util.TextFileReader;

class GradleDependencyTreeOutputParserTest {

	private GradleDependencyTreeOutputParser parserToTest;

	@BeforeEach
	void beforeEach() {
		parserToTest = new GradleDependencyTreeOutputParser();
		parserToTest.parser=new GradleTreeDataParser();

	}

	@Test
	void gradle_simple2_lines_as_expected() throws Exception {
		// @formatter:off
		String data = "+--- org.springframework.boot:spring-boot-starter-data-jpa -> 2.6.4\n"
				    + "+--- org.springframework.boot:spring-boot-starter-web -> 2.6.4";
		// @formatter:on

		/* execute */
		DependaGenModel model = parserToTest.parseDependencyTreeText(data);
		
		/* test */
		assertEquals(2, model.getRootNode().getDependencies().size());
	}
	@Test
	void gradle_3_lines_as_expected() throws Exception {
		// @formatter:off
		String data = 
				  "+--- org.springframework.boot:spring-boot-starter-data-jpa -> 2.6.4\n"
				+ "|    +--- org.springframework.boot:spring-boot-starter-aop:2.6.4\n"
				+ "|    |    +--- org.springframework.boot:spring-boot-starter:2.6.4\n"
				+ "";
		// @formatter:on
		
		/* execute */
		DependaGenModel model = parserToTest.parseDependencyTreeText(data);
		
		/* test */
		RootDependencyNode rootNode = model.getRootNode();
		assertEquals(1, rootNode.getDependencies().size());
		Iterator<DependencyNode> iterator = rootNode.getDependencies().iterator();
		
		DependencyNode next = iterator.next();
		assertEquals(1, next.getDependencies().size());
		
		iterator = next.getDependencies().iterator();
		assertEquals(1, iterator.next().getDependencies().size());
	}
	@Test
	void gradle_4_lines_as_expected() throws Exception {
		// @formatter:off
		String data = 
				  "+--- org.springframework.boot:spring-boot-starter-data-jpa -> 2.6.4\n"
				+ "|    +--- org.springframework.boot:spring-boot-starter-aop:2.6.4\n"
				+ "|    |    +--- org.springframework.boot:spring-boot-starter:2.6.4\n"
				+ "+--- org.springframework.boot:spring-boot-starter-web -> 2.6.4"
				+ "";
		// @formatter:on
		
		/* execute */
		DependaGenModel model = parserToTest.parseDependencyTreeText(data);
		
		/* test */
		RootDependencyNode rootNode = model.getRootNode();
		assertEquals(2, rootNode.getDependencies().size());
		Iterator<DependencyNode> iterator = rootNode.getDependencies().iterator();
		
		DependencyNode next = iterator.next();
		DependencyNode last = iterator.next();
		assertEquals(1, next.getDependencies().size());
		
		iterator = next.getDependencies().iterator();
		assertEquals(1, iterator.next().getDependencies().size());
		
		assertEquals(0, last.getDependencies().size());
	}
	@Test
	void gradle_5_lines_as_expected() throws Exception {
		// @formatter:off
		String data = 
				  "+--- org.springframework.boot:spring-boot-starter-data-jpa -> 2.6.4\n"
				+ "|    +--- org.springframework.boot:spring-boot-starter-aop:2.6.4\n"
				+ "|    |    +--- org.springframework.boot:spring-boot-starter:2.6.4\n"
				+ "+--- org.springframework.boot:spring-boot-starter-web -> 2.6.4\n"
				+ "|    +--- org.springframework.boot:spring-boot-xyz-aop:2.6.4\n"
				+ "";
		// @formatter:on
		
		/* execute */
		DependaGenModel model = parserToTest.parseDependencyTreeText(data);
		
		/* test */
		RootDependencyNode rootNode = model.getRootNode();
		assertEquals(2, rootNode.getDependencies().size());
		Iterator<DependencyNode> rootIterator = rootNode.getDependencies().iterator();
		
		DependencyNode jpaNode = rootIterator.next();
		assertEquals("spring-boot-starter-data-jpa",jpaNode.getArtifact());
		
		DependencyNode webNode = rootIterator.next();
		assertEquals("spring-boot-starter-web",webNode.getArtifact());
		List<DependencyNode> jpaDependencies = jpaNode.getDependencies();
		assertEquals(1, jpaDependencies.size());
		
		Iterator<DependencyNode> rootFirstIterator = jpaDependencies.iterator();
		DependencyNode aopElement = rootFirstIterator.next();
		assertEquals(1, aopElement.getDependencies().size());
		
		assertEquals(1, webNode.getDependencies().size());
	}

	@Test
	void gradle_testdata_1_read_as_expected() throws Exception {
		/* prepare */
		TextFileReader reader = new TextFileReader();
		String output = reader.read(new File("./src/test/resources/gradle/test_runtime_classpath_dependencies_1.txt"));

		/* execute */
		DependaGenModel model = parserToTest.parseDependencyTreeText(output);

		/* test */
		assertNotNull(model);
		RootDependencyNode rootNode = model.getRootNode();

		assertEquals(4, rootNode.getDependencies().size());

	}

}
