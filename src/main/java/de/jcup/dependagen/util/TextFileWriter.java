package de.jcup.dependagen.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class TextFileWriter {

	public void write(File targetFile, String text, boolean overwrite) throws IOException {
		internalWrite(targetFile, text, overwrite, Charset.forName("UTF-8"));
	}

	private void internalWrite(File targetFile, String text, boolean overwrite, Charset charset) throws IOException {
		if (targetFile == null) {
			throw new IllegalArgumentException("null not allowed as file!");
		}
		if (targetFile.exists()) {
			if (!overwrite) {
				return;
			}
			/*
			 * Use old API and not Files.delete(..) - reason: I want not to accidently
			 * delete a folder! With old API it is ensured this is only a file not a dir
			 */
			if (/* NOSONAR */!targetFile.delete()) {
				throw new IOException("was not able to delete existing file:" + targetFile);
			}
		}

		if (!targetFile.exists()) {
			File parentFile = targetFile.getParentFile();
			if (!parentFile.exists() && !parentFile.mkdirs()) {
				throw new IllegalStateException("Not able to create folder structure for:" + targetFile);
			}
			if (!targetFile.createNewFile()) {
				throw new IllegalStateException("was not able to create new file:" + targetFile);
			}
		}
		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(targetFile), charset))) {
			bw.write(text);
		}
	}

}
