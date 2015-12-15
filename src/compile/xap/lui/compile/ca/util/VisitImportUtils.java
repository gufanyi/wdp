/**
 * 
 */
package xap.lui.compile.ca.util;

import java.util.List;
import xap.lui.compile.ca.dom.JProgram;
import xap.lui.compile.ca.jdt.core.dom.ASTNode;
import xap.lui.compile.ca.jdt.core.dom.CompilationUnit;
import xap.lui.compile.ca.jdt.core.dom.Name;
import xap.lui.compile.ca.log.CompileLog;




/**
 * @author chouhl
 *
 */
public class VisitImportUtils {
	
	public static void visitImport(CompilationUnit unit,JProgram jprogram){
		if(unit == null){
			return;
		}
		List ids = unit.imports();
		visitImportDeclarations(ids,jprogram);
	}

	private static void visitImportDeclarations(List<ASTNode> nodes ,JProgram jprogram){
		if(nodes == null){
			return;
		}
		
		for(ASTNode node:nodes){
			visitImportDeclaration(node,jprogram);
		}
	}
	private static void visitImportDeclaration(ASTNode node ,JProgram jprogram){
		if(node == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
		try{
			Name n = (Name)c.getMethod("getName", null).invoke(node, null);
			jprogram.setNodeType(node.getNodeType());
			jprogram.setContentType(JProgram.CONTENT_NAME);
			jprogram.getJimports().add(n.getFullyQualifiedName());
//			VisitUtils.visitName(n,jprogram);
		}catch(Exception e1){CompileLog.info(e1.getMessage());}
	}
}
