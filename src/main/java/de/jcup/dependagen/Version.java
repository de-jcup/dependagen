package de.jcup.dependagen;

import java.io.IOException;
import java.io.InputStream;

import de.jcup.dependagen.util.TextFileReader;

public class Version {
	
	public static final Version INSTANCE = new Version();
	
	private String versionString;
	
	private Version(){
		
	}

	public String getVersionString() {
		if (versionString==null) {
			InputStream inputStream = getClass().getResourceAsStream("/version.txt");
			TextFileReader reader = new TextFileReader();
			try {
				versionString = reader.read(inputStream,"version.txt");
			} catch (IOException e) {
				throw new IllegalStateException("Cannot determine version",e);
			}
		}
		return versionString;
	}
}
