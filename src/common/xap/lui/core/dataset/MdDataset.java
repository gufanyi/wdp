package xap.lui.core.dataset;

import java.lang.reflect.Method;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.design.IDatasetProvider;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.util.ClassUtil;
import xap.lui.core.util.LuiClassUtil;

@XmlRootElement(name = "MdDataset")
@XmlAccessorType(XmlAccessType.NONE)
public class MdDataset extends Dataset {

	private static final long serialVersionUID = 380090536121568068L;
	/* 元数据路径 */
	// @XmlAttribute
	private String objMeta = null;

	/**
	 * 设置objMeta信息
	 * 
	 * @param objMeta
	 */
	public void setObjMeta(String objMeta) {
		this.objMeta = objMeta;
	}

	/**
	 * 获取objMeta的值
	 * 
	 * @return
	 */
	public String getObjMeta() {
		return this.objMeta;
	}

	/**
	 * VoMeta---类全名
	 * 
	 */
	private boolean isLoad = false;

	public void load() {
		this.setVoMeta(this.getVoMeta());
	}

	@Override
	public void setVoMeta(String voMeta) {
		super.setVoMeta(voMeta);
		try {
			if (!this.isLoad) {
				IDatasetProvider provider = (IDatasetProvider) LuiClassUtil.loadClass("xap.lui.core.design.DatasetProviderImpl");
				provider.getMdDataset(this, false);
			}
		} catch (Throwable e) {
			// 此处不能记Log
			throw new LuiRuntimeException("设置MdDataset出错:" + e.getMessage());
		}
		this.isLoad = true;
	}

	public void setVoMeta(String voMeta, boolean needRefNode) {
		super.setVoMeta(voMeta);
		try {
			if (!this.isLoad) {
				IDatasetProvider provider = (IDatasetProvider) LuiClassUtil.loadClass("xap.lui.core.design.DatasetProviderImpl");
				provider.getMdDataset(this, needRefNode);
			}
		} catch (Throwable e) {
			// 此处不能记Log
			throw new LuiRuntimeException("设置MdDataset出错:" + e.getMessage());
		}
		this.isLoad = true;
	}

	@Override
	public List<Field> getFieldList() {
		return super.getFieldList();
	}

	@Override
	public void setFieldList(List<Field> fieldList) {
		List<Field> fs = super.getFieldList();
		if (fs.size() > 0) {
			for (int i = 0; i < fieldList.size(); i++) {
				fs.add(fieldList.get(i));
			}
		} else
			super.setFieldList(fieldList);
	}

	public Object clone() {
		MdDataset ds = (MdDataset) super.clone();
		ds.isLoad = this.isLoad;
		ds.voMeta = this.voMeta;
		return ds;
	}

	public String getTableName() {
		String superClazz = this.getVoMeta();
		Object vo = (Object) ClassUtil.newInstance(superClazz);
		Class<?>[] argsType = null;
		Method method;
		String tableName = null;
		try {
			method = vo.getClass().getMethod("getTableName", argsType);
			Object[] args = null;
			tableName = (String) method.invoke(vo, args);
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage());
		}
		return tableName;
	}

}
