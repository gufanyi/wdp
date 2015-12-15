/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tyeung@bea.com - initial API and implementation
 *    IBM Corporation - implemented methods from IBinding
 *    IBM Corporation - renamed from ResolvedDefaultValuePair to DefaultValuePairBinding
 *******************************************************************************/
package xap.lui.compile.ca.jdt.core.dom;

import xap.lui.compile.ca.jdt.core.dom.BindingResolver;
import xap.lui.compile.ca.jdt.core.dom.IMethodBinding;

/**
 * Member value pair which compose of default values.
 */
class DefaultValuePairBinding extends MemberValuePairBinding {

	private xap.lui.compile.ca.jdt.internal.compiler.lookup.MethodBinding method;

	DefaultValuePairBinding(xap.lui.compile.ca.jdt.internal.compiler.lookup.MethodBinding binding, BindingResolver resolver) {
		super(null, resolver);
		this.method = binding;
		this.value = MemberValuePairBinding.buildDOMValue(binding.getDefaultValue(), resolver);
	}

	public IMethodBinding getMethodBinding() {
		return this.bindingResolver.getMethodBinding(this.method);
	}

	public String getName() {
		return new String(this.method.selector);
	}

	public Object getValue() {
		return this.value;
	}

	public boolean isDefault() {
		return true;
	}

	public boolean isDeprecated() {
		return this.method.isDeprecated();
	}
}
