/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package xap.lui.compile.ca.jdt.internal.compiler.ast;

import xap.lui.compile.ca.jdt.internal.compiler.ASTVisitor;
import xap.lui.compile.ca.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import xap.lui.compile.ca.jdt.internal.compiler.ast.JavadocQualifiedTypeReference;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.BlockScope;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.ClassScope;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.Scope;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.TypeBinding;




public class JavadocArrayQualifiedTypeReference extends ArrayQualifiedTypeReference {

	public int tagSourceStart, tagSourceEnd;

	public JavadocArrayQualifiedTypeReference(JavadocQualifiedTypeReference typeRef, int dim) {
		super(typeRef.tokens, dim, typeRef.sourcePositions);
	}

	protected void reportInvalidType(Scope scope) {
		scope.problemReporter().javadocInvalidType(this, this.resolvedType, scope.getDeclarationModifiers());
	}
	protected void reportDeprecatedType(TypeBinding type, Scope scope) {
		scope.problemReporter().javadocDeprecatedType(type, this, scope.getDeclarationModifiers());
	}

	/* (non-Javadoc)
	 * Redefine to capture javadoc specific signatures
	 * @see eclipse.jdt.internal.compiler.ast.ASTNode#traverse(eclipse.jdt.internal.compiler.ASTVisitor, eclipse.jdt.internal.compiler.lookup.BlockScope)
	 */
	public void traverse(ASTVisitor visitor, BlockScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
	
	public void traverse(ASTVisitor visitor, ClassScope scope) {
		visitor.visit(this, scope);
		visitor.endVisit(this, scope);
	}
}
