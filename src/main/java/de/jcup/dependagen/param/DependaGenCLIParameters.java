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
package de.jcup.dependagen.param;

import com.beust.jcommander.Parameter;

public class DependaGenCLIParameters {

	@Parameter(converter = ActionParamConverter.class, description="action - Options: generate", required=true)
	Action action;
	
	@Parameter(converter = SourceParamConverter.class, names = { "--source", "-s" }, description = "Source, Options: template", required = false)
	Source source = Source.TEMPLATE;

	@Parameter(converter = TemplateParamConverter.class,names = { "--template",
			"-t" }, description = "Template to use.", required = false)
	Template template = Template.SPRING_BOOT;


	@Parameter(converter = TargetParamConverter.class, names = { "--target-output",
			"-o" }, description = "Target output.", required = false)
	Target target = Target.GRADLE;

	@Parameter(names = { "help", "--help" }, description = "Help. Shows help", required = false)
	boolean helpOutputNecessary;

	public Action getAction() {
		return action;
	}

	public Template getTemplate() {
		return template;
	}

	public Source getSource() {
		return source;
	}

	public Target getTarget() {
		return target;
	}

	public boolean isHelpOutputNecessary() {
		return helpOutputNecessary;
	}

}