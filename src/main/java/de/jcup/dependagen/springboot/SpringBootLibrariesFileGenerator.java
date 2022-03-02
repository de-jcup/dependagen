package de.jcup.dependagen.springboot;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.jcup.dependagen.gradle.GradleDependencyTreeOutputParser;
import de.jcup.dependagen.model.DependaGenModel;
import de.jcup.dependagen.util.TextFileReader;
import de.jcup.dependagen.util.TextFileWriter;
import static de.jcup.dependagen.util.OutputConstants.LINE;

public class SpringBootLibrariesFileGenerator {

	public void generate() throws IOException {

		/* generate dependency output */
		File directory = new File("./gradle-templates/spring-boot");
		ProcessBuilder pb = new ProcessBuilder(new File(directory,"generate-gradle-output.sh").getAbsolutePath());
		pb.directory(directory);
		Process process = pb.start();
		try {
			process.waitFor(2, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new IOException("Was not able to wait for result", e);
		}
		TextFileReader reader = new TextFileReader();
		String testRuntimeClasspathTree = reader.read(new File("./gen/gradle-templates/springboot/test_runtime_classpath_dependencies.txt"));

		GradleDependencyTreeOutputParser parser = new GradleDependencyTreeOutputParser();
		DependaGenModel model = parser.parseDependencyTreeText(testRuntimeClasspathTree);

		SpringBootGradleLibrariesSourceFileContentFactory generator = new SpringBootGradleLibrariesSourceFileContentFactory();
		String created = generator.create(model);

		TextFileWriter writer = new TextFileWriter();
		File targetFile = new File("./gen/gradle-templates/springboot/spring_boot_dependagen.gradle");
		writer.write(targetFile, created, true);

		System.out.println(created);
		System.out.println(LINE);
		System.out.println("wrote to " + targetFile.getAbsolutePath());
		System.out.println(LINE);

	}

}
