package de.jcup.dependagen.gradle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.jcup.dependagen.gradle.GradleTreeDataParser.GradleTreeData;

class GradleTreeDataParserTest {

	private GradleTreeDataParser parserToTest;

	@BeforeEach
	void beforeEach() {
		parserToTest = new GradleTreeDataParser();
	}

	@Test
	void empty_string_not_null() throws Exception {
		/* prepare */

		/* execute */
		GradleTreeData data = parserToTest.parse("");

		/* test */
		assertNotNull(data);
		assertFalse(data.isTreeElement);

	}

	@Test
	void standard_version_line() throws Exception {
		/* prepare */

		/* execute */
		GradleTreeData data = parserToTest.parse("|    |    +--- org.junit:junit-bom:5.8.2");

		/* test */
		assertNotNull(data);
		assertEquals("org.junit", data.group);
		assertEquals("junit-bom", data.artifact);
		assertEquals("5.8.2", data.definedVersion);
		assertEquals("5.8.2", data.resultingVersion);
		assertEquals(15, data.column);
	}

	@Test
	void standard_version_line_with_asterisk() throws Exception {
		/* prepare */

		/* execute */
		GradleTreeData data = parserToTest.parse("|    |    +--- org.mockito:mockito-core:4.0.0 (*)");

		/* test */
		assertNotNull(data);
		assertEquals("org.mockito", data.group);
		assertEquals("mockito-core", data.artifact);
		assertEquals("4.0.0", data.definedVersion);
		assertEquals("4.0.0", data.resultingVersion);
		assertEquals(15, data.column);
	}

	@Test
	void spring_boot_starter_web_with_ref() throws Exception {
		/* prepare */

		/* execute */
		GradleTreeData data = parserToTest.parse("+--- org.springframework.boot:spring-boot-starter-web -> 2.6.4");

		/* test */
		assertNotNull(data);
		assertEquals("org.springframework.boot", data.group);
		assertEquals("spring-boot-starter-web", data.artifact);
		assertEquals("2.6.4", data.definedVersion);
		assertEquals("2.6.4", data.resultingVersion);
		assertEquals(5, data.column);
	}

	@Test
	void jackson_datatype_dependency_constraint() throws Exception {
		/* prepare */

		/* execute */
		GradleTreeData data = parserToTest
				.parse("|    |    |    |         +--- com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.13.1 (c)");

		/* test */
		assertNotNull(data);
		assertEquals("com.fasterxml.jackson.datatype", data.group);
		assertEquals("jackson-datatype-jdk8", data.artifact);
		assertEquals("2.13.1", data.definedVersion);
		assertEquals("2.13.1", data.resultingVersion);
		assertEquals(30, data.column);
	}

	@Test
	void jboss_changed_resulting_version() throws Exception {
		/* prepare */

		/* execute */
		GradleTreeData data = parserToTest
				.parse("|    |    |    \\--- org.jboss.logging:jboss-logging:3.3.2.Final -> 3.4.3.Final");

		/* test */
		assertNotNull(data);
		assertEquals("org.jboss.logging", data.group);
		assertEquals("jboss-logging", data.artifact);
		assertEquals("3.3.2.Final", data.definedVersion);
		assertEquals("3.4.3.Final", data.resultingVersion);
		assertEquals(20, data.column);
	}

}
