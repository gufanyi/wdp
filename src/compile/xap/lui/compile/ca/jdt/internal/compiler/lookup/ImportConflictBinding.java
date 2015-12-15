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


import xap.lui.compile.ca.jdt.core.compiler.CharOperation;
import xap.lui.compile.ca.jdt.internal.compiler.ast.ImportReference;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.Binding;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.ImportBinding;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.ReferenceBinding;


public class ImportConflictBinding extends ImportBinding {
public ReferenceBinding conflictingTypeBinding; // must ensure the import is resolved
	
public ImportConflictBinding(char[][] compoundName, Binding methodBinding, ReferenceBinding conflictingTypeBinding, ImportReference reference) {
	super(compoundName, false, methodBinding, reference);
	this.conflictingTypeBinding = conflictingTypeBinding;
}
public char[] readableName() {
	return CharOperation.concatWith(compoundName, '.');
}
public String toString() {
	return "method import : " + new String(readableName()); //$NON-NLS-1$
}
}
