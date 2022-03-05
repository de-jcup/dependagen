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
package de.jcup.dependagen.gradle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.jcup.dependagen.gradle.GradleTreeDataParser.GradleTreeData;
import de.jcup.dependagen.model.DependaGenModel;
import de.jcup.dependagen.model.DependencyNode;

@Component
public class GradleDependencyTreeOutputParser {

	@Autowired
	GradleTreeDataParser parser;
	
	private Map<Integer, DependencyNode> map = new TreeMap<>();

	public DependaGenModel parseDependencyTreeText(String text) {

		DependaGenModel model = new DependaGenModel();
		map.put(0,model.getRootNode());

//		testRuntimeClasspath - Runtime classpath of source set 'test'.
//		+--- org.springframework.boot:spring-boot-starter-data-jpa -> 2.6.4
//		+--- org.springframework.boot:spring-boot-starter-web -> 2.6.4
//		+--- org.springframework.boot:spring-boot-starter-test -> 2.6.4
//		\--- org.springframework.restdocs:spring-restdocs-mockmvc -> 2.0.6.RELEASE

		String[] lines = text.split("\n");

		for (String line : lines) {
			/* first line */
			GradleTreeData newNodeData = parser.parse(line);
			if (!newNodeData.isTreeElement) {
				continue;
			}
			DependencyNode parent = getParentForColumn(newNodeData.column);
			
			DependencyNode newNode = new DependencyNode();
			newNode.setGroup(newNodeData.group);
			newNode.setVersion(newNodeData.definedVersion);
			newNode.setArtifact(newNodeData.artifact);
			
			/* drop all smaller entries, so this node will be the parent at this colum now*/
			dropAllSmallerEntries(newNodeData,newNode);
			
			newNode.setParent(parent);

		}

		return model;
	}
	
	private void dropAllSmallerEntries(GradleTreeData newNodeData, DependencyNode newNode) {
		List<Integer> list = new ArrayList<>(map.keySet());
		for (Integer keyColumn : list) {
			if (keyColumn>=newNodeData.column) {
				map.remove(keyColumn);
			}
		}
		map.put(newNodeData.column, newNode);
	}
	
	
	
	
	private DependencyNode getParentForColumn(int column) {
		DependencyNode defined = map.get(column);
		if (defined!=null) {
			return defined.getParent();
		}
		/* must search*/
		List<Integer> topDownList = new ArrayList<>(map.keySet());
		Collections.sort(topDownList, Collections.reverseOrder());
		for (Integer keyColumn : topDownList) {
			if (keyColumn<=column) {
				DependencyNode resolvedParent = map.get(keyColumn);
				return resolvedParent;
			}
		}
		return null;
	}

}
