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
package de.jcup.dependagen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.beust.jcommander.JCommander;

import de.jcup.dependagen.param.DependaGenCLIParameters;
import de.jcup.dependagen.param.Source;
import de.jcup.dependagen.param.Target;
import de.jcup.dependagen.param.Template;

@Component
public class DependaGenCLIComponent {

	private static final Logger LOG = LoggerFactory.getLogger(DependaGenCLIComponent.class);

	@Autowired
	TemplateFileGeneration templateFileGeneration;
	
	@Autowired
	VersionSupport versionSupport;

	@Bean
	public CommandLineRunner initialIntegrationTestAdmin() {
		return new DependaGenCommandLineRunner();
	}

	public class DependaGenCommandLineRunner implements CommandLineRunner {

		@Override
		public void run(String... args) throws Exception {
			LOG.info("Dependagen starting");
			Console.LOG.info("Version {}", versionSupport.getVersionString());
			
			DependaGenCLIParameters arguments = new DependaGenCLIParameters();

			JCommander commander = JCommander.newBuilder().programName("dependagen").addObject(arguments).build();
			
			try {
				commander.parse(args);
			} catch (Exception e) {
				Console.LOG.error("Unsupported arguments:"+e.getMessage());
				commander.usage();
				return;
			}

			if (arguments.isHelpOutputNecessary()) {
				commander.usage();
				return;
			}

			try {
				/* action */
				switch (arguments.getAction()) {
				case GENERATE_FILE:
					generateFile(arguments, commander);
					break;
				default:
					failWithUnsupportedArgumentCombination("Action " + arguments.getAction() + " is not accepted",
							commander);
				}
			} catch (Exception e) {
				Console.LOG.error("Execution failed - {}", e.getMessage());
				LOG.error("Execution failed", e);

				System.exit(2);
			}

		}

	}

	public void generateFile(DependaGenCLIParameters arguments, JCommander commander) throws Exception {
		Target target = arguments.getTarget();
		switch (target) {
		case GRADLE:
			generateGradleFileFromSource(arguments,commander);
			break;
		default:
			failWithUnsupportedArgumentCombination("given target " + target + " is not supported",
					commander);
			break;

		}
	}

	private void generateGradleFileFromSource(DependaGenCLIParameters arguments, JCommander commander) throws Exception {
		Source source = arguments.getSource();
		
		switch (source) {
		case TEMPLATE:
			generateGradleFileFromTemplateSource(arguments,commander);
			break;
		default:
			failWithUnsupportedArgumentCombination("given source " + source + " is not supported",
					commander);
			break;

		}

	}

	private void generateGradleFileFromTemplateSource(DependaGenCLIParameters arguments, JCommander commander) throws Exception {
		Template template = arguments.getTemplate();
		
		switch (template) {
		case SPRING_BOOT:
			templateFileGeneration.generateSpringBootTemplateGradleOutputFile();
			break;
		default:
			failWithUnsupportedArgumentCombination("given target " + template + " is not supported",
					commander);
			break;

		}

		
	}
	
	private void failWithUnsupportedArgumentCombination(String message, JCommander commander) {
		Console.LOG.info("Given argument combination not valid - reason:" + message);
		commander.usage();
		System.exit(1);

	}
}
