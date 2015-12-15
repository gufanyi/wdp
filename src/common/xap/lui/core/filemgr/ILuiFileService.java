package xap.lui.core.filemgr;

import xap.lui.core.exception.LuiBusinessException;
import xap.lui.core.vos.LuiFileVO;

/**
 * LUI文件操作接口
 * 
 * @author licza
 * 
 */
public interface ILuiFileService {
	String add(LuiFileVO vo) throws LuiBusinessException;

	void delete(LuiFileVO vo) throws LuiBusinessException;

	void edit(LuiFileVO vo) throws LuiBusinessException;
	
	void updataVos(LuiFileVO[] vos) throws LuiBusinessException;
	
}
