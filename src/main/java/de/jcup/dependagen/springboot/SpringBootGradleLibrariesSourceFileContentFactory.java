package de.jcup.dependagen.springboot;

import de.jcup.dependagen.model.DependaGenModel;
import de.jcup.dependagen.model.DependencyNode;
import de.jcup.dependagen.model.RootDependencyNode;

public class SpringBootGradleLibrariesSourceFileContentFactory {

	public String create(DependaGenModel model) {
		StringBuilder sb = new StringBuilder();
		
		RootDependencyNode rootNode = model.getRootNode();
		append(sb,rootNode);
		
		
		return sb.toString();
	}
	
	private void append(StringBuilder sb, DependencyNode parent) {

		for (DependencyNode node: parent.getDependencies()) {
			String group = node.getGroup();
			if  (group.indexOf("springframework")!=-1) {
				append(sb,node);
			}else {
				String id = createLibraryId(node);
				sb.append(id).append("=").append("'").append(node.getGroup()).append(":").append(node.getArtifact()).append(":").append(node.getVersion()).append("'\n");
				node.getVersion();
			}
		}
		
	}
	
	
	private String createLibraryId(DependencyNode node) {
		return node.getArtifact().toLowerCase().replaceAll("\\.","_").replaceAll("-", "_");
	}

}
