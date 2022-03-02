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
package de.jcup.dependagen.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class TextFileReader {
	
	public String read(InputStream inputStream, String streamInfo) throws IOException {
		return read(inputStream,"\n",streamInfo);
	}
	
	public String read(File file) throws IOException{
		return read(file, "\n");
	}

	public String read(File file, String lineBreak) throws IOException{
		Path path = file.toPath().toAbsolutePath();

		try(FileInputStream inputStream = new FileInputStream(path.toFile())){
			return read(inputStream, lineBreak,"File "+file.getName());
		} catch (Exception e) {
			throw new IOException("Cannot fetch inputream of test file " + file.getAbsolutePath(), e);
		}
	}

	public String read(InputStream inputStream, String lineBreak, String streamInfo) throws IOException {
		if (inputStream==null) {
			throw new IllegalArgumentException("Input stream for '"+streamInfo+"' was null"); 
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
			StringBuilder sb = new StringBuilder();
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
			throw new IOException("Cannot read " + streamInfo, e);
		}
	}
}