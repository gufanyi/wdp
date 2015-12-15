package xap.lui.core.refrence;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.logger.LuiLogger;
import xap.lui.core.util.LuiClassUtil;
import xap.mw.core.data.BizException;
import xap.mw.coreitf.d.FBoolean;
import xap.mw.sf.core.util.ServiceFinder;
import xap.sys.bdrefinfo.d.BdRefInfoDO;
import xap.sys.bdrefinfo.i.IBdrefinfoRService;
public class RefPubUtil {
	
	private static Map<String, BdRefInfoDO> cache = new HashMap<String, BdRefInfoDO>();
	
	private static BdRefInfoDO[] infoVOs = null;
	
	
	public static int getRefType(IRefModel model) {
		int refType = IRefConst.GRID;
		if (model instanceof IRefTreeGridModel) {
			refType = IRefConst.GRIDTREE;
		} else if (model instanceof IRefTreeModel) {
			refType = IRefConst.TREE;
		}
		return refType;
	}
	
	public static void clearRefInfoVOs() {
		cache.clear();
		infoVOs = null;
	}
	
	public static BdRefInfoDO getRefinfoVO(String refCode) {
		if(StringUtils.isEmpty(refCode)) return null;
		BdRefInfoDO vo = getRefNodeCodeVSRefInfoVO().get(refCode);
		if (vo == null) {
			try {
				IBdrefinfoRService server = ServiceFinder.find(IBdrefinfoRService.class);
				try {
					BdRefInfoDO[] refInfos = server.find("code='"+refCode+"'", "", FBoolean.FALSE);
					if(refInfos!=null&&refInfos.length>0){
						vo=refInfos[0];
					}else{
						return null;
//						throw new RuntimeException(refCode+"类型的参照不存在");
					}
					getRefNodeCodeVSRefInfoVO().put(refCode, vo);
				} catch (BizException e1) {
					throw new RuntimeException(e1.getMessage());
				}
			} catch (Throwable e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return vo;
	}
	
	public static BdRefInfoDO getRefinfoVO(String metadatatypename,String para1) {
		if(StringUtils.isEmpty(metadatatypename)) return null;
			try {
				String sql = " metadatatypename='"+metadatatypename+"'";
				if(metadatatypename.equals("xap.sys.xbd.udi.d.UdidocDO")) {
					sql += " and para1 = '"+para1+"'";
				}
				IBdrefinfoRService server = ServiceFinder.find(IBdrefinfoRService.class);
				try {
					BdRefInfoDO[] refInfos = server.find(sql, "", FBoolean.FALSE);
					if(refInfos!=null&&refInfos.length>0){
						BdRefInfoDO vo=refInfos[0];
						return vo;
					}else{
						LuiLogger.error(metadatatypename+"类型的参照不存在");
					}
				} catch (BizException e) {
					throw new RuntimeException(e.getMessage());
				}
			} catch (Throwable e) {
				throw new RuntimeException(e.getMessage());
			}
			return null;
	}
	
	
	private static Map<String, BdRefInfoDO> getRefNodeCodeVSRefInfoVO() {
		BdRefInfoDO[] vos = getAllRefInfoVOs();
		for (int i = 0; i < vos.length; i++) {
			cache.put(vos[i].getCode(), vos[i]);
		}
		return cache;
	}
	
	
	public static  IRefModel getRefModel(String refCode) {
		String refClassName = getRefModelClazz(refCode);
		if(refClassName == null) return null;
		Object obj=LuiClassUtil.loadClass(refClassName);
		//obj.getClass().getSuperclass().getSuperclass().getInterfaces()[0].getClassLoader()
				//xap.sys.appfw.refinfo.IRefGridModel.class.getClassLoader()
		 if((obj instanceof xap.sys.appfw.refinfo.IRefTreeGridModel)){
			XapRefTreeGridModelAdapter adapter=new XapRefTreeGridModelAdapter();
			xap.sys.appfw.refinfo.IRefTreeGridModel xapModel=(xap.sys.appfw.refinfo.IRefTreeGridModel)obj;
			adapter.setXapRefModel(xapModel);
			return  adapter;
		}
		
	   else if((obj instanceof xap.sys.appfw.refinfo.IRefTreeModel)){
			XapRefTreeModelAdapter adapter=new XapRefTreeModelAdapter();
			xap.sys.appfw.refinfo.IRefTreeModel xapModel=(xap.sys.appfw.refinfo.IRefTreeModel)obj;
			adapter.setXapRefModel(xapModel);
			return  adapter;
		}else  if((obj instanceof xap.sys.appfw.refinfo.IRefGridModel)){
			XapRefGridModelAdapter adapter=new XapRefGridModelAdapter();
			xap.sys.appfw.refinfo.IRefGridModel xapModel=(xap.sys.appfw.refinfo.IRefGridModel)obj;
			adapter.setXapRefModel(xapModel);
			return  adapter;
		}
		return  (IRefModel)LuiClassUtil.loadClass(refClassName);
	}
	
	public static  IRefModel getRefModel(String metadatatypename,String para1) {
		String refClassName = getRefModelClazz(metadatatypename,para1);
		if(refClassName == null) return null;
		Object obj=LuiClassUtil.loadClass(refClassName);
	    if((obj instanceof xap.sys.appfw.refinfo.IRefGridModel)){
			XapRefGridModelAdapter adapter=new XapRefGridModelAdapter();
			xap.sys.appfw.refinfo.IRefGridModel xapModel=(xap.sys.appfw.refinfo.IRefGridModel)obj;
			adapter.setXapRefModel(xapModel);
			return  adapter;
		}else if((obj instanceof xap.sys.appfw.refinfo.IRefTreeModel)){
			XapRefTreeModelAdapter adapter=new XapRefTreeModelAdapter();
			xap.sys.appfw.refinfo.IRefTreeModel xapModel=(xap.sys.appfw.refinfo.IRefTreeModel)obj;
			adapter.setXapRefModel(xapModel);
			return  adapter;
		}else if((obj instanceof xap.sys.appfw.refinfo.IRefTreeGridModel)){
			XapRefTreeGridModelAdapter adapter=new XapRefTreeGridModelAdapter();
			xap.sys.appfw.refinfo.IRefTreeGridModel xapModel=(xap.sys.appfw.refinfo.IRefTreeGridModel)obj;
			adapter.setXapRefModel(xapModel);
			return  adapter;
		}
		return  (IRefModel)LuiClassUtil.loadClass(refClassName);
	}
	
	
	public static  String getRefModelClazz(String metadatatypename,String para2) {
		if (StringUtils.isBlank(metadatatypename)) {
			return null;
		}
		BdRefInfoDO vo = getRefinfoVO(metadatatypename,para2);
		String modelClassName = null;
		if (vo != null) {
			modelClassName = vo.getRefclass();
		}
		return modelClassName;
	}
	
	
	public static  String getRefModelClazz(String refCode) {
		if (StringUtils.isBlank(refCode)) {
			return null;
		}
		BdRefInfoDO vo = getRefinfoVO(refCode);
		String modelClassName = null;
		if (vo != null) {
			modelClassName = vo.getRefclass();
		}
		return modelClassName;
	}
	
	
	private static synchronized BdRefInfoDO[] getAllRefInfoVOs() {
		Collection<BdRefInfoDO> col = null;
		if (infoVOs != null) {
			return infoVOs;
		}
		col = getRefInfoVOsFromDB();
		if (col != null && col.size() > 0) {
			infoVOs = new BdRefInfoDO[col.size()];
			col.toArray(infoVOs);
		}
		return infoVOs;
	}
	
	
	private static Collection<BdRefInfoDO> getRefInfoVOsFromDB() {
		IBdrefinfoRService server = ServiceFinder.find(IBdrefinfoRService.class);
		try {
			BdRefInfoDO[] refInfos = server.find("1=1", "", FBoolean.FALSE);
			return Arrays.asList(refInfos);
		} catch (BizException e1) {
			throw new RuntimeException(e1.getMessage());
		}
	}
}
