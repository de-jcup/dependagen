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
package de.jcup.dependagen.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.dependagen.Console;

public abstract class AbstractErrorHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractErrorHandler.class);

	public final void handleError(String message, int exitValue) {
		Console.LOG.error(message);
		LOG.error("Handling error:{}, wanted exit value:{}", message, exitValue);

		handleSystemExit(exitValue);
	}

	protected abstract void handleSystemExit(int exitValue);
	
}
