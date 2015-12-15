package xap.lui.compile.ca.test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import xap.lui.compile.ca.dom.JClass;
import xap.lui.compile.ca.dom.JField;
import xap.lui.compile.ca.dom.JMethod;
import xap.lui.compile.ca.dom.JProgram;
import xap.lui.compile.ca.dom.JStatement;
import xap.lui.compile.ca.jdt.core.dom.CompilationUnit;
import xap.lui.compile.ca.log.CompileLog;
import xap.lui.compile.ca.translate.TranslateUtils;
import xap.lui.compile.ca.util.CompilationJavaUtils;


public class Lui_XAP_Compile{
	
	public static void showMapMsg(Map<String, String> map){
		if(map!=null){
			Iterator<String> ptkeys = map.keySet().iterator();
			String ptkey = null;
			while(ptkeys.hasNext()){
				ptkey = ptkeys.next();
				CompileLog.debug(ptkey+" "+map.get(ptkey));
			}
		}
	}
	
	public static void main(String[] args){
		String classname = "";
		String methodname = "onclick";
		
		Lui_XAP_Compile gwt = new Lui_XAP_Compile();
		gwt.compilerJavaToJavascript(classname, methodname);
	}
	
	private Map<String, JClass> jclassMap = new LinkedHashMap<String, JClass>();
	
	public void compilerJavaToJavascript(String wholeClassName,String methodName){
		
		if(wholeClassName != null && wholeClassName.startsWith("java")){
			return;
		}
		
		wholeClassName = wholeClassName.replaceFirst("<.{0,}>", "");
		
		String[] filename = CompilationJavaUtils.getFilename(wholeClassName);
		
		CompilationUnit unit = CompilationJavaUtils.parseJavaToTree(filename);

		JProgram jp = new JProgram();
		JMethod method = new JMethod();
		method.setWholeClassName(wholeClassName);
		method.setMethodname(methodName);
		jp.setMethod(method);
		
		CompilationJavaUtils.visitCompilationUnit(unit,jp);
		
		CompilationJavaUtils.dealWithJProgram(jp);
		
//		showJProgramMsg(jp);
		
		/******/
		JClass jclass = this.jclassMap.get(wholeClassName);
		if(jclass == null){
			jclass = new JClass();
		}
		jclass.setFieldList(jp.getClassMap().get(wholeClassName).getFieldMapToList());
		//jclass.getMethodList().add(method);
		this.jclassMap.put(wholeClassName, jclass);
		/******/
		
		List<JClass> jcs = jp.getClassMapToList();
		for(JClass jc:jcs){
			List<JMethod> jms = jc.getMethodMapToList();
			for(JMethod jm:jms){
				if(jm.getMethodname().equals(methodName)){
					jclass.getMethodList().add(jm);
//					method = jm;
//					method.setParamTypeNames(jm.getParamTypeNames());
//					method.setStatementMap(jm.getStatementMap());
				}
				List<JStatement> jss = jm.getStatementMapToList();
				for(JStatement js:jss){
					List<JMethod> jmes = js.getMethodMapToList();
					for(JMethod jme:jmes){
						compilerJavaToJavascript(jme.getWholeClassName(), jme.getMethodname(), jme.getParamTypeNames());
					}
				}
			}
		}
		
		//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000000")/*需要翻译的所有类方法和类属性*/);
		Iterator<String> keys = this.jclassMap.keySet().iterator();
		String key = null;
		while(keys.hasNext()){
			key = keys.next();
			JClass jc = this.jclassMap.get(key);
			//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000001")/*---类：*/+key);
			if(jc != null){
				
				TranslateUtils.translate(key, jc.getFieldList(), jc.getMethodList());
				
				List<JField> jfs = jc.getFieldList();
				for(JField jf:jfs){
					//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000002")/*---类属性：*/+jf.getTypename().getOriginalValue() + " --- " + jf.getFieldname().getOriginalValue());
				}
				List<JMethod> jms = jc.getMethodList();
				for(JMethod jm:jms){
					String[] paramTypeNames = jm.getParamTypeNames();
					String param = "";
					if(paramTypeNames != null){
						StringBuffer s = new StringBuffer();
						for(String p:paramTypeNames){
							s.append(p + ",");
						}
						if(s.length() > 0){
							param = s.substring(0, s.length()-1);
						}
					}
					//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000003")/*---类方法：*/+jm.getMethodname() + "("+param+")");
				}
			}
		}
	}
	
	private void compilerJavaToJavascript(String wholeClassName,String methodName,String[] paramTypeNames){
		
		if(wholeClassName != null && wholeClassName.startsWith("java")){
			return;
		}
		wholeClassName = CompilationJavaUtils.getRealWholeClassName(wholeClassName, methodName, paramTypeNames);
		
		wholeClassName = wholeClassName.replaceFirst("<.{0,}>", "");
		/********/
		JClass jclass = this.jclassMap.get(wholeClassName);
		if(jclass == null){
			jclass = new JClass();
		}else{
			List<JMethod> methods = jclass.getMethodList();
			for(JMethod method:methods){
				if(method.getMethodname() != null && method.getMethodname().equals(methodName)){//方法名相同
					String[] ptns = method.getParamTypeNames();
					if((ptns == null || ptns.length == 0) && (paramTypeNames == null || paramTypeNames.length == 0)){//方法无入参
						return;
					}else if((ptns != null && paramTypeNames != null && ptns.length == paramTypeNames.length)){//入参个数相同
						boolean temp = true;
						for(int i=0;i<ptns.length;i++){
							if(!(ptns[i] != null && ptns[i].equals(paramTypeNames[i]))){//入参类型不同
								temp = false;
								break;
							}
						}
						if(temp){//入参类型相同
							return;
						}
					}
				}
			}
		}
		/******/
		String[] filename = CompilationJavaUtils.getFilename(wholeClassName);
		
		CompilationUnit unit = CompilationJavaUtils.parseJavaToTree(filename);
		
		JProgram jp = new JProgram();
		JMethod method = new JMethod();
		method.setWholeClassName(wholeClassName);
		method.setMethodname(methodName);
		method.setParamTypeNames(paramTypeNames);
		jp.setMethod(method);
		
		CompilationJavaUtils.visitCompilationUnit(unit,jp);
		
		CompilationJavaUtils.dealWithJProgram(jp);
		
//		showJProgramMsg(jp);
		
		/******/
		jclass.setFieldList(jp.getClassMap().get(wholeClassName).getFieldMapToList());
		//jclass.getMethodList().add(method);
		this.jclassMap.put(wholeClassName, jclass);
		/******/
		
		List<JClass> jcs = jp.getClassMapToList();
		for(JClass jc:jcs){
			List<JMethod> jms = jc.getMethodMapToList();
			for(JMethod jm:jms){
				if(jm.getMethodname().equals(methodName)){
					jclass.getMethodList().add(jm);
//					method.setParamMap(jm.getParamMap());
//					method.setStatementMap(jm.getStatementMap());
				}
				List<JStatement> jss = jm.getStatementMapToList();
				for(JStatement js:jss){
					List<JMethod> jmes = js.getMethodMapToList();
					for(JMethod jme:jmes){
						compilerJavaToJavascript(jme.getWholeClassName(), jme.getMethodname(), jme.getParamTypeNames());
					}
				}
			}
		}
	}
	
	private void showJProgramMsg(JProgram jp){
		CompileLog.debug("*********************************************");
		List<JClass> jcs = jp.getClassMapToList();
		for(JClass jc:jcs){
			//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000004")/******类属性******/);
			List<JField> jfs = jc.getFieldMapToList();
			for(JField jf:jfs){
				CompileLog.debug(jf.getTypename().getOriginalValue() + " " + jf.getFieldname().getOriginalValue());
			}
			
			//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000005")/******类方法******/);
			List<JMethod> jms = jc.getMethodMapToList();
			for(JMethod jm:jms){
				//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000006")/******方法参数******/);
				showMapMsg(jm.getParamTypeMap());
				//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000007")/******方法内部变量******/);
				showMapMsg(jm.getFieldTypeMap());
//				CaLogger.debug("*****方法内部方法*****");
//				showMapMsg(jm.getMethodTypeMap());
//				CaLogger.debug("*****方法内部类*****");
//				showMapMsg(jm.getClassTypeMap());
				
				//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000008")/******方法内部Statement******/);
				List<JStatement> jss = jm.getStatementMapToList();
				for(JStatement js:jss){
					//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000009")/*源码*/+js.getStatement());
					//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000010")/******Statement内部方法******/);
					List<JMethod> jmes = js.getMethodMapToList();
					for(JMethod jme:jmes){
						//CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000009")/*源码*/+jme.getExpression());
						if(jme.getObjname() != null){
							//CompileLog.debug(jme.getObjname().getOriginalValue() + NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000011")/* 调用 */ + jme.getMethodname());
						}else{
						//	CompileLog.debug(NCLangRes4VoTransl.getNCLangRes().getStrByID("ca", "GWT_UAP-000012")/*this 调用 */ + jme.getMethodname());
						}
						String[] ptns = jme.getParamTypeNames();
						for(String ptn:ptns){
							CompileLog.debug(ptn);
						}
					}
				}
			}
		}
	}
}
