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
package xap.lui.compile.ca.jdt.core.dom;


import xap.lui.compile.ca.jdt.internal.compiler.ASTVisitor;
import xap.lui.compile.ca.jdt.internal.compiler.ast.ConstructorDeclaration;
import xap.lui.compile.ca.jdt.internal.compiler.ast.FieldDeclaration;
import xap.lui.compile.ca.jdt.internal.compiler.ast.Initializer;
import xap.lui.compile.ca.jdt.internal.compiler.ast.MethodDeclaration;
import xap.lui.compile.ca.jdt.internal.compiler.ast.TypeDeclaration;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.ClassScope;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.CompilationUnitScope;
import xap.lui.compile.ca.jdt.internal.compiler.lookup.MethodScope;

class NodeSearcher extends ASTVisitor {
	public xap.lui.compile.ca.jdt.internal.compiler.ast.ASTNode found;
	public TypeDeclaration enclosingType;
	public int position;
	
	NodeSearcher(int position) {
		this.position = position;
	}

	public boolean visit(
		ConstructorDeclaration constructorDeclaration,
		ClassScope scope) {

		if (constructorDeclaration.declarationSourceStart <= position
			&& position <= constructorDeclaration.declarationSourceEnd) {
				found = constructorDeclaration;
				return false;
		}
		return true;
	}

	public boolean visit(
		FieldDeclaration fieldDeclaration,
		MethodScope scope) {
			if (fieldDeclaration.declarationSourceStart <= position
				&& position <= fieldDeclaration.declarationSourceEnd) {
					found = fieldDeclaration;
					return false;
			}
			return true;
	}

	public boolean visit(Initializer initializer, MethodScope scope) {
		if (initializer.declarationSourceStart <= position
			&& position <= initializer.declarationSourceEnd) {
				found = initializer;
				return false;
		}
		return true;
	}

	public boolean visit(
		TypeDeclaration memberTypeDeclaration,
		ClassScope scope) {
			if (memberTypeDeclaration.declarationSourceStart <= position
				&& position <= memberTypeDeclaration.declarationSourceEnd) {
					enclosingType = memberTypeDeclaration;
					return true;

			}
			return false;		
	}

	public boolean visit(
		MethodDeclaration methodDeclaration,
		ClassScope scope) {

		if (methodDeclaration.declarationSourceStart <= position
			&& position <= methodDeclaration.declarationSourceEnd) {
				found = methodDeclaration;
				return false;
		}
		return true;
	}

	public boolean visit(
		TypeDeclaration typeDeclaration,
		CompilationUnitScope scope) {
			if (typeDeclaration.declarationSourceStart <= position
				&& position <= typeDeclaration.declarationSourceEnd) {
					enclosingType = typeDeclaration;
					return true;
			}
			return false;		
	}

}
