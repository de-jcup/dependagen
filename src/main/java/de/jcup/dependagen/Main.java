package de.jcup.dependagen;

import de.jcup.dependagen.springboot.SpringBootLibrariesFileGenerator;

public class Main {

	public static void main(String[] args) throws Exception{
		SpringBootLibrariesFileGenerator libraryFilesGenerator = new SpringBootLibrariesFileGenerator();
		libraryFilesGenerator.generate();
	}
	
}
