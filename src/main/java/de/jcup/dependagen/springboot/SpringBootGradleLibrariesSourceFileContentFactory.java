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
package de.jcup.dependagen.springboot;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.jcup.dependagen.VersionSupport;
import de.jcup.dependagen.model.DependaGenModel;
import de.jcup.dependagen.model.DependencyNode;
import de.jcup.dependagen.model.RootDependencyNode;
import de.jcup.dependagen.util.DependaGenStringUtil;

@Component
public class SpringBootGradleLibrariesSourceFileContentFactory {

	@Autowired
	VersionSupport versionSupport;
	
	@Autowired
	SpringBootFlatDependencyMapFactory factory;
	
	private static final String INDENTION = "     ";
	
	public String create(DependaGenModel model) {
		StringBuilder sb = new StringBuilder();

		RootDependencyNode rootNode = model.getRootNode();
		for (DependencyNode node : rootNode.getDependencies()) {
			if ("org.springframework.boot".equals(node.getGroup())) {
				model.setComment("Next parts are dependencies used by Spring Boot v " + node.getVersion());
				break;
			}
		}
		
		Map<String, DependencyNode> map = factory.create(model);

		sb.append("ext {\n");
		sb.append("  spring_boot_dependency = [\n");
		sb.append("     // ").append(model.getComment()).append("\n");
		sb.append("     // You can use this information to use exact same library version inside\n");
		sb.append("     // your own libraries, where you have no spring boot dependency management\n");
		sb.append("     //\n");
		sb.append("     // Generated by dependagen "+versionSupport.getVersionString()+"\n");
		sb.append("     // (see https://github.com/de-jcup/dependagen)\n\n");

		createDependencies(map, sb);
		sb.append("  ]\n\n");
		sb.append("  spring_boot_dependency_version = [\n");
		createVersionsOfDependencies(map, sb);
		sb.append("  ]\n");
		sb.append("}");
		return sb.toString();
	}

	private void createDependencies(Map<String, DependencyNode> map, StringBuilder sb) {
		for (Iterator<String> kit = map.keySet().iterator(); kit.hasNext();) {
			String key = kit.next();
			DependencyNode node = map.get(key);
			sb.append(INDENTION);
			DependaGenStringUtil.appendFilledString(sb, key + ":", 40);
			sb.append("'").append(node.getGroup()).append(":").append(node.getArtifact()).append(":")
					.append(node.getVersion()).append("'");
			if (kit.hasNext()) {
				sb.append(",");
			}
			sb.append("\n");

		}
	}

	private void createVersionsOfDependencies(Map<String, DependencyNode> map, StringBuilder sb) {
		for (Iterator<String> kit = map.keySet().iterator(); kit.hasNext();) {
			String key = kit.next();
			DependencyNode node = map.get(key);
			sb.append(INDENTION);
			DependaGenStringUtil.appendFilledString(sb, key + ":", 40);
			sb.append("'").append(node.getVersion()).append("'");
			if (kit.hasNext()) {
				sb.append(",");
			}
			sb.append("\n");

		}
	}


	


}
