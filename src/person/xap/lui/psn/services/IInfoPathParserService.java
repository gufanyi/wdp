package xap.lui.psn.services;

import xap.lui.psn.context.ParserResult;


/**
 * infopath表单内容view.xsl文件解析类对应接口
 * 
 * @author liujmc
 * @date 2012-08-20
 */
public interface IInfoPathParserService {
	
	/**
	 * 创建主要步骤：
	 * 1：构造UIMeta和LuiWidget对象
	 * 2：创建主要框架布局，infopath主要用table和div布局，抽取table下属标签和div作为gridlayout和flowvlayout的骨架
	 * 3：构造主要控件内容，主控件和内容在最后一层td或者div中
	 * 4：构建uimeta.um和widget.wd文件
	 * @throws Exception
	 */
	public void parseInfoPathContent(String sourceFilePath,String targetFilePath) throws Exception;
	
	/**
	 * 解析xsl，返回widget和uimeta的集合类
	 * 
	 * @param xslString
	 * @return ParserResult
	 * @throws Exception
	 */
	public ParserResult parseInfoPathContent(String xslString) throws Exception;
}
