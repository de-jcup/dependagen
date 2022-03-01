package de.jcup.dependagen.model;

public class DependaGenModel {

	private RootDependencyNode rootNode = new RootDependencyNode();
	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public RootDependencyNode getRootNode() {
		return rootNode;
	}

	
}
