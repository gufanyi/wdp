package xap.lui.core.j2eesvr;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.lang.StringUtils;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiState;
import xap.lui.core.common.ServerConfig;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.plugins.ILuiPaltformExtProvier;
import xap.lui.core.plugins.LuiPaltformContranier;
import xap.lui.core.util.WFWUtil;
/**
 * lui启动时执行。仅lui模块本身使用
 */
public final class LuiServerListener extends BaseServerListener {
	public LuiServerListener(ServletContext ctx) {
		super(ctx);
	}
	protected void doAfterStarted() {
		try {
			ServletContext ctx = getCtx();
			LuiRuntimeContext.setLuiServletContext(ctx);
			String isStartCache = ctx.getInitParameter("isStartCache");
			if(StringUtils.isNotBlank(isStartCache)){
				if (Boolean.valueOf(isStartCache)) {
					LuiRuntimeContext.isStartCache = true;
				} else {
					LuiRuntimeContext.isStartCache = false;
				}
			}else{
				LuiRuntimeContext.isStartCache = false;
			}
			ServerConfig serverConf = new ServerConfig(ctx);
			LuiRuntimeContext.setServerConfig(serverConf);
			if (WFWUtil.isDebugModel()) {
				// preCompress();
			}
		} catch (Exception e) {
			throw new LuiRuntimeException(e.getMessage());
		} finally {
			LuiRuntimeContext.setLuiState(LuiState.STARTED);
		}
		LuiPaltformContranier.getInstance();
		ClassPool pool = ClassPool.getDefault();
		try {
			// 把classpath的导入
			pool.appendClassPath(new ClassClassPath(LuiServerListener.class));
			// pool.importPackage("xap.lui.core.model");
			CtClass cc = pool.get("xap.lui.core.model.ViewPartComps");
			ClassFile classFile = cc.getClassFile();
			// 获取常量池
			ConstPool constPool = classFile.getConstPool();
			// 获取声明的components-field
			List fields = classFile.getFields();
			FieldInfo compInfo = null;
			for (int i = 0; i < fields.size(); i++) {
				FieldInfo fieldInfo = (FieldInfo) fields.get(i);
				String fieldName = fieldInfo.getName();
				if ("components".equalsIgnoreCase(fieldName)) {
					compInfo = fieldInfo;
					classFile.getFields().remove(fieldInfo);
					break;
				}
			}
			AnnotationsAttribute attribute = (AnnotationsAttribute) compInfo.getAttribute(AnnotationsAttribute.visibleTag);
			Annotation[] annto = attribute.getAnnotations();
			ArrayMemberValue arrayMemberValue = (ArrayMemberValue) annto[0].getMemberValue("value");
			MemberValue[] values = (MemberValue[]) arrayMemberValue.getValue();
			List<MemberValue> list = new ArrayList<MemberValue>();
			for (int i = 0; i < values.length; i++) {
				list.add(values[i]);
			}
			// 动态
			ILuiPaltformExtProvier[] providers = LuiPaltformContranier.getInstance().getProvideres();
			for (int i = 0; i < providers.length; i++) {
				ILuiPaltformExtProvier inner = providers[i];
				Annotation tableAnnotation = new Annotation("javax.xml.bind.annotation.XmlElementRef", constPool);
				tableAnnotation.addMemberValue("name", new StringMemberValue(inner.getCompTypeName(), constPool));
				tableAnnotation.addMemberValue("type", new ClassMemberValue(inner.getWebCompClazz().getName(), constPool));
				AnnotationMemberValue test = new AnnotationMemberValue(tableAnnotation, constPool);
				list.add(test);
				arrayMemberValue.setValue(list.toArray(new AnnotationMemberValue[0]));
			}
			attribute.setAnnotation(annto[0]);
			compInfo.addAttribute(attribute);
			attribute = (AnnotationsAttribute) compInfo.getAttribute(AnnotationsAttribute.visibleTag);
			classFile.getFields().add(compInfo);
			cc.writeFile();
			ClassLoader loader = LuiServerListener.class.getClassLoader();
			Class theClazz = cc.toClass(loader, null);
			Field[] fieldes = theClazz.getDeclaredFields();
			Field theField = null;
			for (int i = 0; i < fieldes.length; i++) {
				String name = fieldes[i].getName();
				if ("components".equalsIgnoreCase(name)) {
					theField = fieldes[i];
				}
			}
			theField.getAnnotations();
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
		}
	}
}
