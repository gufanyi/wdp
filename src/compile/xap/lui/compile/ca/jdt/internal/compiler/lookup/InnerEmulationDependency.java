/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package xap.lui.compile.ca.jdt.internal.compiler.lookup;

import xap.lui.compile.ca.jdt.internal.compiler.lookup.BlockScope;

public class InnerEmulationDependency{

	public BlockScope scope;
	public boolean wasEnclosingInstanceSupplied;
    
	public InnerEmulationDependency(BlockScope scope, boolean wasEnclosingInstanceSupplied) {
		this.scope = scope;
		this.wasEnclosingInstanceSupplied = wasEnclosingInstanceSupplied;
	}
}
