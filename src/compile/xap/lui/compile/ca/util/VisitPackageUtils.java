/**
 * 
 */
package xap.lui.compile.ca.util;


import xap.lui.compile.ca.dom.JProgram;
import xap.lui.compile.ca.jdt.core.dom.ASTNode;
import xap.lui.compile.ca.jdt.core.dom.CompilationUnit;
import xap.lui.compile.ca.jdt.core.dom.Name;
import xap.lui.compile.ca.jdt.core.dom.PackageDeclaration;
import xap.lui.compile.ca.log.CompileLog;


/**
 * @author chouhl
 *
 */
public class VisitPackageUtils {
	
	public static void visitPackage(CompilationUnit unit,JProgram jprogram){
		if(unit == null){
			return;
		}
		PackageDeclaration pd = unit.getPackage();
		visitPackageDeclaration(pd,jprogram);
	}

	private static void visitPackageDeclaration(ASTNode node ,JProgram jprogram){
		if(node == null){
			return;
		}
		
		Class c = ASTNode.nodeClassForType(node.getNodeType());
		
//		try{
//			List<Annotation> anns = (List<Annotation>)c.getMethod("annotations", null).invoke(node, null);
//			visitAnnotations(anns,jprogram);
//		}catch(Exception e1){CaLogger.info(e1.getMessage());}
		
		try{
			Name n = (Name)c.getMethod("getName", null).invoke(node, null);
			jprogram.setNodeType(node.getNodeType());
			jprogram.setContentType(JProgram.CONTENT_NAME);
			jprogram.setJpackage(n.getFullyQualifiedName());
//			VisitUtils.visitName(n, jprogram);
		}catch(Exception e1){CompileLog.info(e1.getMessage());}
	}
}
