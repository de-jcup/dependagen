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
package de.jcup.dependagen.util;

public class DependaGenStringUtil {
	
	public static void appendFilledString(StringBuilder sb, String text, int fillUpToLength) {

		StringBuilder sb2 = new StringBuilder();
		sb2.append(text);
		while (fillUpToLength - sb2.length() > 0) {
			sb2.append(' ');
		}
		sb.append(sb2);
	}
}
