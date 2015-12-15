/**
 * 
 */
package xap.lui.compile.ca.util;

import java.util.List;
import xap.lui.compile.ca.ast.StatementSymbol;
import xap.lui.compile.ca.ast.ThenStatementSymbol;
import xap.lui.compile.ca.jdt.core.dom.ASTNode;
import xap.lui.compile.ca.jdt.core.dom.Block;
import xap.lui.compile.ca.jdt.core.dom.Statement;




/**
 * @author chouhl
 *
 */
public class VisitIfStatementUtils {

	public static StatementSymbol visitThenStatement(Statement st){
		StatementSymbol ss = null;
		switch(st.getNodeType()){
			case ASTNode.BLOCK:
				Block b = (Block)st;
				
				ss = new ThenStatementSymbol();
				
				List<Statement> sts = b.statements();
				for(Statement s:sts){
					((ThenStatementSymbol)ss).getStatementSymbolList().add(visitStatement(s));
				}
				break;
			default:
				break;
		}
		return ss;
	}
	
	private static StatementSymbol visitStatement(Statement st){
		StatementSymbol ss = null;
		
		return ss;
	}
}
