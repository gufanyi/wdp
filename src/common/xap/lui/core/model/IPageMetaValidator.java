package xap.lui.core.model;

import xap.lui.core.exception.LuiValidateException;

public interface IPageMetaValidator {
	public void validate(PagePartMeta pageMeta) throws LuiValidateException; 
}
