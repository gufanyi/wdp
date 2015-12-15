/**
 * 
 */
package xap.lui.compile.ca.util;

import java.util.List;
import xap.lui.compile.ca.dom.JClass;
import xap.lui.compile.ca.dom.JProgram;
import xap.lui.compile.ca.jdt.core.dom.ASTNode;
import xap.lui.compile.ca.jdt.core.dom.AbstractTypeDeclaration;
import xap.lui.compile.ca.jdt.core.dom.CompilationUnit;
import xap.lui.compile.ca.jdt.core.dom.SimpleName;
import xap.lui.compile.ca.jdt.core.dom.Type;
import xap.lui.compile.ca.log.CompileLog;



/**
 * @author chouhl
 *
 */
public class VisitClassUtils {
	
	public static void visitClass(CompilationUnit unit,JProgram jprogram){
		if(unit == null){
			return;
		}
		List atds = unit.types();
		visitAbstractTypeDeclarations(atds,jprogram);
	}

	private static void visitAbstractTypeDeclarations(List<AbstractTypeDeclaration> atds ,JProgram jprogram){
		if(atds == null){
			return;
		}
		for(AbstractTypeDeclaration atd:atds){
			visitAbstractTypeDeclaration(atd,jprogram);
		}
	}
	private static void visitAbstractTypeDeclaration(AbstractTypeDeclaration node ,JProgram jprogram){
		if(node == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		/**
		 * 类名称 已处理：TypeDeclaration 未处理：EnumDeclaration AnnotationTypeDeclaration
		 */
		try{
			SimpleName sn = (SimpleName)c.getMethod("getName", null).invoke(node, null);
			jprogram.setNodeType(node.getNodeType());
			jprogram.setContentType(JProgram.CONTENT_NAME);
			JClass jc = new JClass();
			jc.setClassname(sn.getFullyQualifiedName());
			jprogram.getClassMap().put(CompilationJavaUtils.getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jc.getClassname()), jc);
			jprogram.setCurrentClassKey(CompilationJavaUtils.getWholeClassName(jprogram.getJimports(), jprogram.getJpackage(), jc.getClassname()));
		}catch(Exception e1){
			CompileLog.info(e1.getMessage());
		}
		/**
		 * 父类名称
		 */
		try{
			Type t = (Type)c.getMethod("getSuperclassType", null).invoke(node, null);
			if(t != null){
				jprogram.getCurrentClass().setSuperclassname(t.toString().replaceFirst("[<].{1,}[>]", ""));
			}
		}catch(Exception e1){
			CompileLog.info(e1.getMessage());
		}
		/**
		 * 类成员变量
		 */
		VisitFieldUtils.visitFieldDeclaration(node, jprogram);
		
		/**
		 * 类成员方法
		 */
		VisitMethodUtils.visitMethodDeclaration(node, jprogram);
		
	}
}
