package de.jcup.dependagen.gradle;

public class GradleTreeDataParser {

	/**
	 * Example: "+--- org.springframework.boot:spring-boot-starter-data-jpa ->
	 * 2.6.4" will be parsed to: group: org.springframework.boot, artefact:
	 * spring-boot-starter-data-jpa, version:2.6.4
	 * 
	 * 
	 * "+--- org.junit.platform:junit-platform-engine:1.8.2 (*)" will be parsed to
	 * group: org.junit.platform, artifact: junit-platform-engine, version:1.8.2
	 * 
	 * @param line
	 * @return
	 */
	public GradleTreeData parse(String line) {
		GradleTreeData data = new GradleTreeData();
		if (line.startsWith("+") || line.startsWith("|") || line.startsWith("\\")) {
			data.isTreeElement = true;
		}
		if (!data.isTreeElement) {
			return data;
		}
		for (char c : line.toCharArray()) {
			if (c == '|' || c == '+' || c == '-' || c == '\\' || Character.isWhitespace(c)) {
				data.column++;
				continue;
			}
			break;
		}
		String text = line.substring(data.column);

		String[] splitted = text.split(":");
		data.group = splitted[0];
		if (splitted.length > 1) {
			data.artifact = splitted[1];
		}else {
			return data;
		}
		if (splitted.length > 2) {
			data.definedVersion = splitted[2];
		}else {
			// org.springframework.boot:spring-boot-starter-data-jpa -> 2.6.4
			String combined = data.artifact;
			String[] splittedArtifact = combined.split("->");
			data.artifact=splittedArtifact[0].trim();
			data.definedVersion=splittedArtifact[1].trim();
		}
		
		/* handle special cases in version*/
		int indexOfBracket = data.definedVersion.indexOf("(");
		if (indexOfBracket!=-1) {
			data.definedVersion=data.definedVersion.substring(0,indexOfBracket-1);
		}
		data.definedVersion=data.definedVersion.trim();
		String[] splittedVersion = data.definedVersion.split("->");
		if (splittedVersion.length>1) {
			data.definedVersion=splittedVersion[0].trim();
			data.resultingVersion=splittedVersion[1].trim();
		}
		
		if (data.resultingVersion == null) {
			data.resultingVersion = data.definedVersion;
		}
		return data;
	}

	public static class GradleTreeData {
		@Override
		public String toString() {
			return "GradleTreeData [column=" + column + ", isTreeElement=" + isTreeElement + ", group=" + group
					+ ", artifact=" + artifact + ", definedVersion=" + definedVersion + ", resultingVersion="
					+ resultingVersion + "]";
		}
		int column;
		boolean isTreeElement;

		String group;
		String artifact;
		String definedVersion;
		String resultingVersion;
	}
}
