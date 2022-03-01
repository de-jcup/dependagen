package de.jcup.dependagen.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class TextFileReader {
	
	public String read(File file) {
		return read(file, "\n");
	}

	public String read(File file, String lineBreak) {
		Path path = file.toPath().toAbsolutePath();
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), "UTF-8"))) {
			String line = null;

			boolean firstEntry = true;
			while ((line = br.readLine()) != null) {
				if (!firstEntry) {
					sb.append(lineBreak);
				}
				sb.append(line);
				firstEntry = false;// this prevents additional line break at end of file...
			}
			return sb.toString();
		} catch (Exception e) {
			throw new IllegalStateException("Cannot read test file " + file.getAbsolutePath(), e);
		}
	}
}