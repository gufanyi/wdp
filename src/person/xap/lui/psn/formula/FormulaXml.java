package xap.lui.psn.formula;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.exception.LuiRuntimeException;

@XmlRootElement(name = "formulaxml")
@XmlAccessorType(XmlAccessType.NONE)
public class FormulaXml {

	@XmlElement(name="category")
	private List<Category> categoryList = null;

	public void addCategory(Category category) {
		if (this.categoryList == null)
			this.categoryList = new ArrayList<Category>();
		this.categoryList.add(category);
	}
	
	public Category getCategory(String id) {
		if (categoryList == null)
			return null;
		for (Category p : categoryList) {
			if (id.equals(p.getId())) {
				return p;
			}
		}
		return null;
	}
	
	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
	
	public static FormulaXml parse(InputStream input) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(FormulaXml.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			FormulaXml conf = (FormulaXml) jaxbUnmarshaller.unmarshal(new StringReader(ContextResourceUtil.inputStream2String(input)));
			return conf;
		} catch (Throwable e) {
			throw new LuiRuntimeException(e);
		}
	}
}
