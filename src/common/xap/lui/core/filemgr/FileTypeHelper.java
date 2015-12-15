package xap.lui.core.filemgr;

import java.util.Map;

import xap.lui.core.vos.FileTypeVO;


public class FileTypeHelper {
	public final static String ViewType_Office = "office";
	public final static String ViewType_Img = "img";
	public final static String ViewType_Txt = "txt";
	
	public final static String FileType_sign = "sign";
	private static Map<String,FileTypeVO> maps = null;
	private static Map<String,FileTypeVO> getmaps(){
		if(maps == null){
			synchronized(FileTypeHelper.class){
				if(maps == null){
//					try {
//						List<FileTypeVO> list = (List<FileTypeVO>) PtBaseDAO.getIns().retrieveAll(FileTypeVO.class);
//						if(null != list){
//							maps = new HashMap<String,FileTypeVO>();
//							for(FileTypeVO vo : list){
//								if(maps.containsKey(vo.getName()))
//									continue;
//								maps.put(vo.getName(), vo);
//							}
//						}
//					} catch (Throwable e) {
//						LuiLogger.error(e);
//					}
					
				}
			}
		}
		return maps;
	}
	public static FileTypeVO getFiletype(String filetype){
		if(null == filetype || filetype.equals("") )
			return null;
		Map<String,FileTypeVO> types = getmaps();
		if(null == types)
			return null;
		if(!types.containsKey(filetype.toLowerCase()))
			return null;
		else
			return types.get(filetype.toLowerCase());
	}
	public static String getContentType(String filetype){
		FileTypeVO vo = getFiletype(filetype);
		if(null != vo)
			return vo.getMime();
		else
			return "";
	}
}
