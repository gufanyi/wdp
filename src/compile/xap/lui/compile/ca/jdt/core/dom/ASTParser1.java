package xap.lui.compile.ca.jdt.core.dom;

import java.util.Map;
import xap.lui.compile.ca.core.runtime.IProgressMonitor;
import xap.lui.compile.ca.internal.compiler.problem.DefaultProblemFactory;
import xap.lui.compile.ca.internal.compiler.problem.ProblemReporter;
import xap.lui.compile.ca.internal.core.util.CommentRecorderParser;
import xap.lui.compile.ca.jdt.core.ICompilationUnit;
import xap.lui.compile.ca.jdt.core.WorkingCopyOwner;
import xap.lui.compile.ca.jdt.internal.compiler.CompilationResult;
import xap.lui.compile.ca.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import xap.lui.compile.ca.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import xap.lui.compile.ca.jdt.internal.compiler.impl.CompilerOptions;
import xap.lui.compile.ca.jdt.internal.compiler.parser.Parser;
import xap.lui.compile.ca.jdt.internal.core.BasicCompilationUnit;
import xap.lui.compile.ca.jdt.internal.core.DefaultWorkingCopyOwner;

public class ASTParser1 {
	/**
	 * Kind constant used to request that the source be parsed as a single
	 * expression.
	 */
	public static final int K_EXPRESSION = 0x01;

	/**
	 * Kind constant used to request that the source be parsed as a sequence of
	 * statements.
	 */
	public static final int K_STATEMENTS = 0x02;

	/**
	 * Kind constant used to request that the source be parsed as a sequence of
	 * class body declarations.
	 */
	public static final int K_CLASS_BODY_DECLARATIONS = 0x04;

	/**
	 * Kind constant used to request that the source be parsed as a compilation
	 * unit.
	 */
	public static final int K_COMPILATION_UNIT = 0x08;

	/**
	 * Creates a new object for creating a Java abstract syntax tree (AST)
	 * following the specified set of API rules.
	 * 
	 * @param level
	 *            the API level; one of the LEVEL constants declared on
	 *            <code>AST</code>
	 * @return new ASTParser instance
	 */
	public static ASTParser1 newParser() {
		return new ASTParser1();
	}

	/**
	 * Level of AST API desired.
	 */
	private final int apiLevel = 3;;

	/**
	 * Kind of parse requested. Defaults to an entire compilation unit.
	 */
	private int astKind;

	/**
	 * Compiler options. Defaults to JavaCore.getOptions().
	 */
	private Map compilerOptions;

	// /**
	// * Request for a partial AST. Defaults to <code>false</code>.
	// */
	// private boolean partial = false;
	//
	// /**
	// * Request for a statements recovery. Defaults to <code>false</code>.
	// */
	// private boolean statementsRecovery;
	//
	// /**
	// * Request for a bindings recovery. Defaults to <code>false</code>.
	// */
	// private boolean bindingsRecovery;
	//
	// /**
	// * The focal point for a partial AST request.
	// * Only used when <code>partial</code> is <code>true</code>.
	// */
	// private int focalPointPosition;

	/**
	 * Source string.
	 */
	private char[] rawSource = null;

	/**
	 * Java model class file or compilation unit supplying the source.
	 */
	// private ITypeRoot typeRoot = null;
	// /**
	// * Character-based offset into the source string where parsing is to
	// * begin. Defaults to 0.
	// */
	// private int sourceOffset = 0;
	//
	// /**
	// * Character-based length limit, or -1 if unlimited.
	// * All characters in the source string between <code>offset</code>
	// * and <code>offset+length-1</code> inclusive are parsed. Defaults to -1,
	// * which means the rest of the source string.
	// */
	// private int sourceLength = -1;
	// /**
	// * Working copy owner. Defaults to primary owner.
	// */
	private WorkingCopyOwner workingCopyOwner = DefaultWorkingCopyOwner.PRIMARY;

	//
	// /**
	// * Java project used to resolve names, or <code>null</code> if none.
	// * Defaults to none.
	// */
	// private IJavaProject project = null;

	/**
	 * Name of the compilation unit for resolving bindings, or <code>null</code>
	 * if none. Defaults to none.
	 */
	// private String unitName = null;
	/**
	 * Creates a new AST parser for the given API level.
	 * <p>
	 * N.B. This constructor is package-private.
	 * </p>
	 * 
	 * @param level
	 *            the API level; one of the LEVEL constants declared on
	 *            <code>AST</code>
	 */
	ASTParser1() {
		initializeDefaults();
	}

	/**
	 * Sets all the setting to their default values.
	 */
	private void initializeDefaults() {
		this.astKind = K_COMPILATION_UNIT;
		this.rawSource = null;
	}

	/**
	 * Creates an abstract syntax tree.
	 * <p>
	 * A successful call to this method returns all settings to their default
	 * values so the object is ready to be reused.
	 * </p>
	 * 
	 * @param monitor
	 *            the progress monitor used to report progress and request
	 *            cancelation, or <code>null</code> if none
	 * @return an AST node whose type depends on the kind of parse requested,
	 *         with a fallback to a <code>CompilationUnit</code> in the case of
	 *         severe parsing errors
	 * @exception IllegalStateException
	 *                if the settings provided are insufficient, contradictory,
	 *                or otherwise unsupported
	 */
	public ASTNode createAST(IProgressMonitor monitor) {
		ASTNode result = null;
		if (monitor != null)
			monitor.beginTask("", 1); //$NON-NLS-1$
		try {
			if (this.rawSource == null) {
				throw new IllegalStateException("source not specified"); //$NON-NLS-1$
			}
			result = internalCreateAST(monitor);
		} finally {
			// re-init defaults to allow reuse (and avoid leaking)
			initializeDefaults();
			if (monitor != null)
				monitor.done();
		}
		return result;
	}

	private ASTNode internalCreateAST(IProgressMonitor monitor) {
		switch (this.astKind) {

		case K_COMPILATION_UNIT:
			CompilationUnitDeclaration compilationUnitDeclaration = null;
			try {
				NodeSearcher searcher = null;
				xap.lui.compile.ca.jdt.internal.compiler.env.ICompilationUnit sourceUnit = null;
				WorkingCopyOwner wcOwner = this.workingCopyOwner;
				if (this.rawSource != null) {
					sourceUnit = new BasicCompilationUnit(this.rawSource, null, ""); //$NON-NLS-1$ this.unitName == null ? "" : this.unitName
				}
				int flags = 0;
				compilationUnitDeclaration = parse(sourceUnit, searcher, this.compilerOptions, flags);
				CompilationUnit result = convert(compilationUnitDeclaration, sourceUnit.getContents(), this.apiLevel, this.compilerOptions, false, wcOwner, flags, monitor);
				result.setTypeRoot(null);// this.typeRoot
				return result;
			} finally {

			}
		}
		throw new IllegalStateException();
	}

	public static CompilationUnit convert(CompilationUnitDeclaration compilationUnitDeclaration, char[] source, int apiLevel, Map options, boolean needToResolveBindings, WorkingCopyOwner owner,
			int flags, IProgressMonitor monitor) {
		BindingResolver resolver = null;
		AST ast = AST.newAST(apiLevel);
		ast.setDefaultNodeFlag(ASTNode.ORIGINAL);
		CompilationUnit compilationUnit = null;
		ASTConverter converter = new ASTConverter(options, needToResolveBindings, monitor);
		resolver = new BindingResolver();
		ast.setFlag(flags);
		ast.setBindingResolver(resolver);
		converter.setAST(ast);
		compilationUnit = converter.convert(compilationUnitDeclaration, source);
		compilationUnit.setLineEndTable(compilationUnitDeclaration.compilationResult.getLineSeparatorPositions());
		ast.setDefaultNodeFlag(0);
		ast.setOriginalModificationCount(ast.modificationCount());
		return compilationUnit;
	}

	/**
	 * Sets the source code to be parsed.
	 * 
	 * @param source
	 *            the source string to be parsed, or <code>null</code> if none
	 */
	public void setSource(char[] source) {
		this.rawSource = source;
		// clear the type root
		// this.typeRoot = null;
	}

	public static CompilationUnitDeclaration parse(xap.lui.compile.ca.jdt.internal.compiler.env.ICompilationUnit sourceUnit, NodeSearcher nodeSearcher, Map settings, int flags) {
		if (sourceUnit == null) {
			throw new IllegalStateException();
		}
		CompilerOptions compilerOptions = new CompilerOptions(settings);
		boolean statementsRecovery = (flags & ICompilationUnit.ENABLE_STATEMENTS_RECOVERY) != 0;
		compilerOptions.performMethodsFullRecovery = statementsRecovery;
		compilerOptions.performStatementsRecovery = statementsRecovery;
		ProblemReporter report = new ProblemReporter(DefaultErrorHandlingPolicies.proceedWithAllProblems(), compilerOptions, new DefaultProblemFactory());
		Parser parser = new CommentRecorderParser(report, false);
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0, compilerOptions.maxProblemsPerUnit);
		CompilationUnitDeclaration compilationUnitDeclaration = parser.dietParse(sourceUnit, compilationResult);
		if (compilationUnitDeclaration.ignoreMethodBodies) {
			compilationUnitDeclaration.ignoreFurtherInvestigation = true;
			return null;
		}
		if (nodeSearcher != null) {
		} else {
			xap.lui.compile.ca.jdt.internal.compiler.ast.TypeDeclaration[] types = compilationUnitDeclaration.types;
			if (types != null) {
				for (int i = 0, length = types.length; i < length; i++)
					types[i].parseMethods(parser, compilationUnitDeclaration);
			}
		}
		return compilationUnitDeclaration;
	}

}
