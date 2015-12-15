package xap.lui.psn.java;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import xap.lui.compile.ca.jdt.core.dom.AST;
import xap.lui.compile.ca.jdt.core.dom.ASTNode;
import xap.lui.compile.ca.jdt.core.dom.ASTParser1;
import xap.lui.compile.ca.jdt.core.dom.AbstractTypeDeclaration;
import xap.lui.compile.ca.jdt.core.dom.Block;
import xap.lui.compile.ca.jdt.core.dom.BodyDeclaration;
import xap.lui.compile.ca.jdt.core.dom.CompilationUnit;
import xap.lui.compile.ca.jdt.core.dom.IExtendedModifier;
import xap.lui.compile.ca.jdt.core.dom.ImportDeclaration;
import xap.lui.compile.ca.jdt.core.dom.MethodDeclaration;
import xap.lui.compile.ca.jdt.core.dom.Modifier.ModifierKeyword;
import xap.lui.compile.ca.jdt.core.dom.PrimitiveType;
import xap.lui.compile.ca.jdt.core.dom.SingleVariableDeclaration;
import xap.lui.compile.ca.jdt.core.dom.Type;
import xap.lui.compile.ca.jdt.core.dom.TypeDeclaration;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.LuiEventConf;
public class CreateJavaUtil {
	public static final String CLASS_ControllerTemplate = "LuiCtrlTpl.ftl";
	public static final String Resouce_Folder = "xap/lui/psn/java";
	public static String getControllerClazz(String packageName, String className) {
		String templateClassName = Resouce_Folder + "/" + CLASS_ControllerTemplate;
		String content = ContextResourceUtil.getResourceAsString(templateClassName,false);
		CompilationUnit unit = getASTUnit(content);
		AST ast = unit.getAST();
		// 修改包名
		if (packageName == null || packageName.trim().length() == 0) {
			unit.getPackage().delete();
		} else {
			unit.getPackage().setName(ast.newName(packageName.trim()));
		}
		/**
		 * 只修改public类
		 */
		TypeDeclaration td = getFirstPublicClass(unit);
		// 修改类名
		if (className != null && className.trim().length() > 0) {
			td.setName(ast.newSimpleName(className));
		}
		// 修改构造方法名
		MethodDeclaration[] mds = td.getMethods();
		for (MethodDeclaration md : mds) {
			if (md.isConstructor()) {
				md.setName(ast.newSimpleName(className));
			}
		}
		StringBuffer classContent = new StringBuffer(unit.toString());
		replaceMethodBody(classContent, mds);
		return classContent.toString();
	}
	/**
	 * 创建AST编译单元对象
	 * 
	 * @param javaFilePath
	 * @return
	 */
	private static CompilationUnit getASTUnit(String javaContent) {
		CompilationUnit unit = null;
		try {
			String content = javaContent;
			char[] ch = new char[content.length()];
			content.getChars(0, content.length(), ch, 0);
			// 获取模板java类AST对象
			ASTParser1 astParser = ASTParser1.newParser();
			astParser.setSource(ch);
			unit = (CompilationUnit) astParser.createAST(null);
		} catch (Exception e) {
			throw new LuiRuntimeException(e.getMessage());
		}
		return unit;
	}
	public static TypeDeclaration getFirstPublicClass(CompilationUnit unit) {
		TypeDeclaration td = null;
		List<AbstractTypeDeclaration> atds = unit.types();
		if (atds != null && atds.size() > 0) {
			jump: for (AbstractTypeDeclaration atd : atds) {
				if (atd instanceof TypeDeclaration) {
					td = (TypeDeclaration) atd;
					List<IExtendedModifier> iems = td.modifiers();
					if (iems != null && iems.size() > 0) {
						for (IExtendedModifier iem : iems) {
							if (iem.isModifier() && iem.toString().equals(ModifierKeyword.PUBLIC_KEYWORD.toString())) {
								break jump;
							}
						}
					}
					td = null;
				}
			}
		}
		return td;
	}
	private static void replaceMethodBody(StringBuffer classContent, MethodDeclaration[] mds) {
		if (mds != null && mds.length > 0) {
			for (MethodDeclaration md : mds) {
				if (md.getMs() != null) {
					int start = classContent.indexOf(md.getMs()) - 1;
					int end = start + md.getMs().length() + 3;
					classContent.replace(start, end, md.getMethodBody().trim());
				}
			}
		}
	}
	/**
	 * 创建AST方法对象，方法体为空
	 * 
	 * @param ast
	 * @param methodName
	 * @param paramList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static MethodDeclaration createNewMethod(AST ast, String methodName, LuiEventConf eventConf) {
		// 新建AST方法对象
		MethodDeclaration newMethod = ast.newMethodDeclaration();
		// 设置方法访问权限为public
		List<IExtendedModifier> ems = newMethod.modifiers();
		if (ems == null) {
			ems = new ArrayList<IExtendedModifier>();
		}
		ems.add(ast.newModifier(ModifierKeyword.PUBLIC_KEYWORD));
		// 返回值
		Type returnType = ast.newPrimitiveType(PrimitiveType.VOID);
		newMethod.setReturnType2(returnType);
		// methodName
		newMethod.setName(ast.newSimpleName(methodName));
		List<SingleVariableDeclaration> params = newMethod.parameters();
		if (params == null) {
			params = new ArrayList<SingleVariableDeclaration>();
		}
		SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
		svd.setName(ast.newSimpleName("event"));
		String type = eventConf.getEventType();
		svd.setType(ast.newSimpleType(ast.newName(type.substring(type.lastIndexOf(".")+1,type.length()))));
		params.add(svd);
		// // params
		// if (paramList != null && paramList.size() > 0) {
		// List<SingleVariableDeclaration> params = newMethod.parameters();
		// if (params == null) {
		// params = new ArrayList<SingleVariableDeclaration>();
		// }
		// for (LuiParameter param : paramList) {
		// SingleVariableDeclaration svd = ast.newSingleVariableDeclaration();
		// svd.setName(ast.newSimpleName(param.getName()));
		// svd.setType(ast.newSimpleType(ast.newName(param.getValue().substring(param.getValue().lastIndexOf(".")
		// + 1))));
		// params.add(svd);
		// }
		// }
		// body
		Block body = ast.newBlock();
		newMethod.setBody(body);
		return newMethod;
	}
	/**
	 * 操作事件方法
	 * 
	 * @param amcServiceObj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String operateMethod(String cmmandName, String javaContent, String tplName, LuiEventConf event) {
		CompilationUnit unit = getASTUnit(javaContent);
		AST ast = unit.getAST();
		// 获取类文件所有导入类
		List<ImportDeclaration> imports = unit.imports();
		if (imports == null) {
			imports = new ArrayList<ImportDeclaration>();
		}
		// old
		Map<String, ImportDeclaration> importsMap = new HashMap<String, ImportDeclaration>();
		for (ImportDeclaration id : imports) {
			importsMap.put(id.getName().getFullyQualifiedName(), id);
		}
		// 获取类文件public类
		TypeDeclaration td = getFirstPublicClass(unit);
		List<BodyDeclaration> bodys = td.bodyDeclarations();
		if (bodys == null) {
			bodys = new ArrayList<BodyDeclaration>();
		}
		switch (event.getEventStatus()) {
		case LuiEventConf.ADD_STATUS:
			MethodDeclaration newMethod = createNewMethod(ast, event.getMethod(), event);
			// 重新设置body
			Block body = ast.newBlock();
			Map<String, MethodDeclaration> mt = getMethodTemplate(tplName,event);
			MethodDeclaration md = mt.get(cmmandName);
			if (md != null) {
				body = (Block) ASTNode.copySubtree(body.getAST(), md.getBody());
				importsMap.putAll(getImportTemplate(tplName,event));
				newMethod.setMethodBody(md.getMethodBody());
				newMethod.setMs(md.getMs());
			}
			newMethod.setBody(body);
			bodys.add(newMethod);
			break;
		case LuiEventConf.DEL_STATUS:
			for (int i = 0; i < bodys.size(); i++) {
				if (bodys.get(i) instanceof MethodDeclaration) {
					if (((MethodDeclaration) bodys.get(i)).getName().getFullyQualifiedName().equals(event.getMethod())) {
						bodys.remove(i);
						break;
					}
				}
			}
			break;
		default:
			break;
		}
		// 重新设置类文件导入类
		imports.clear();
		Iterator<String> keys = importsMap.keySet().iterator();
		while (keys.hasNext()) {
			ImportDeclaration id = ast.newImportDeclaration();
			id = (ImportDeclaration) ASTNode.copySubtree(id.getAST(), importsMap.get(keys.next()));
			imports.add(id);
		}
		StringBuffer classContent = new StringBuffer(unit.toString());
		replaceMethodBody(classContent, td.getMethods());
		return classContent.toString();
	}
	public static Map<String, MethodDeclaration> getMethodTemplate(String tplName,LuiEventConf event) {
//		String templateClassName = Resouce_Folder + "/" + tplName;
//		String content = ContextResourceUtil.getResourceAsString(templateClassName);
		
		Map<String, Object> context = new HashMap<String, Object>();
		List<LuiParameter> paraList = event.getExtendParamList();
		for (LuiParameter tmp : paraList) {
			String key = tmp.getName();
			String value = tmp.getValue();
			if (key.endsWith("_Exattr")) {
				key = key.substring(0, key.indexOf("_Exattr"));
			}
			context.put(key, value);
		}
		String newContent = JavaFreeMarkerUtil.processTemplate(tplName, context);
		
		Map<String, MethodDeclaration> mt = new HashMap<String, MethodDeclaration>();
		CompilationUnit unit = getASTUnit(newContent);
		TypeDeclaration td = getFirstPublicClass(unit);
		MethodDeclaration[] mds = td.getMethods();
		if (mds != null && mds.length > 0) {
			for (MethodDeclaration md : mds) {
				mt.put(md.getName().getFullyQualifiedName(), md);
			}
		}
		return mt;
	}
	public static Map<String, ImportDeclaration> getImportTemplate(String tplName,LuiEventConf event) {
		Map<String, Object> context = new HashMap<String, Object>();
		List<LuiParameter> paraList = event.getExtendParamList();
		for (LuiParameter tmp : paraList) {
			String key = tmp.getName();
			String value = tmp.getValue();
			if (key.endsWith("_Exattr")) {
				key = key.substring(0, key.indexOf("_Exattr"));
			}
			context.put(key, value);
		}
		String newContent = JavaFreeMarkerUtil.processTemplate(tplName, context);
		Map<String, ImportDeclaration> imports = new HashMap<String, ImportDeclaration>();
		List<ImportDeclaration> ids = null;
		CompilationUnit unit = getASTUnit(newContent);
		ids = unit.imports();
		if (ids != null && ids.size() > 0) {
			for (ImportDeclaration importDecl : ids) {
				imports.put(importDecl.getName().getFullyQualifiedName(), importDecl);
			}
		}
		return imports;
	}
}
