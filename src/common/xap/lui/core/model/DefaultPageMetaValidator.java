package xap.lui.core.model;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import xap.lui.core.exception.LuiValidateException;


public class DefaultPageMetaValidator implements IPageMetaValidator {

	public void validate(PagePartMeta pageMeta) throws LuiValidateException {
		//首先校验是否能序列化，否则集群下会出现问题
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream writer = new ObjectOutputStream(out);
			writer.writeObject(pageMeta);
		}
		catch(Exception e){
			throw new LuiValidateException(e.getMessage());
		}
	}

}
