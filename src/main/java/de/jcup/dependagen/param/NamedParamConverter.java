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

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public abstract class NamedParamConverter<T extends NamedParam> implements IStringConverter<T> {

	private T[] acceptedValues;

	public NamedParamConverter(T[] acceptedParams) {
		this.acceptedValues = acceptedParams;
	}

	@Override
	public T convert(String value) {
		String infoName = null;
		for (T acceptedParam : acceptedValues) {
			if (infoName == null) {
				infoName = acceptedParam.getClass().getSimpleName().toLowerCase();
			}

			if (acceptedParam.getParamName().equalsIgnoreCase(value)) {
				return acceptedParam;
			}
		}
		throw new ParameterException(
				value + " is not an accepted " + infoName + ". Accepted is only:" + describe(acceptedValues));
	}

	protected String describe(T[] params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.length; i++) {
			sb.append(params[i].getParamName());
			if (i < params.length - 1) {
				sb.append("|");
			}
		}
		return sb.toString();
	}
}
