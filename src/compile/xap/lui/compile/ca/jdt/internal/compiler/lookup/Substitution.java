/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package xap.lui.compile.ca.jdt.internal.compiler.lookup;

import xap.lui.compile.ca.jdt.internal.compiler.lookup.LookupEnvironment;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.TypeBinding;

/*
 * Encapsulates aspects related to type variable substitution
 */
public interface Substitution {
    
	/**
	 * Returns the type substitute for a given type variable, or itself
	 * if no substitution got performed.
	 */
	TypeBinding substitute(TypeVariableBinding typeVariable);
	
	/**
	 * Returns the lookup environment
	 */
	LookupEnvironment environment();
	
	/**
	 * Returns true for raw substitution
	 */
	boolean isRawSubstitution();
}
