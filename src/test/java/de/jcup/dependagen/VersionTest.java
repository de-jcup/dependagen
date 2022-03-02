package de.jcup.dependagen;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class VersionTest {

	@Test
	void verson_string_is_not_null() {
		assertNotNull(Version.INSTANCE.getVersionString());
	}

}
