package xap.lui.core.builder;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.format.AddressFormatMeta;
import xap.lui.core.format.CurrencyFormatMeta;
import xap.lui.core.format.DateFormatMeta;
import xap.lui.core.format.FormatDocVO;
import xap.lui.core.format.FormatMeta;
import xap.lui.core.format.NumberFormatMeta;
import xap.lui.core.format.TimeFormatMeta;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.sso.SessionBean;
import xap.lui.core.xml.StringUtils;

public class LuiMaskerUtil {
	/** 默认左键 **/
	private static final String DEFAULTKEYLEFT = "default";
	/** 默认右键 **/
	private static final long DEFAULTKEYRIGHT = 0L;
	/** 缓存 **/
	private static final String PUB_MASKER_CACHE = "PUB_MASKER_CACHE";
	/**
	 * 注册Masker信息缓存
	 * @param md
	 */
	@SuppressWarnings("unchecked")
	public static void registerMasker(FormatDocVO md){
		String keyL = DEFAULTKEYLEFT;
		Long keyR = DEFAULTKEYRIGHT;
		try {
			String dsName = LuiRuntimeContext.getDatasource();
			LUICache pubCache = CacheMgr.getStrongCache(PUB_MASKER_CACHE, dsName);
			if(md != null && md.getPk_formatdoc() != null){
				keyL = md.getPk_formatdoc();
				//keyR = md.getTs().getMillis();
			} 
			Map<Long,String> maskerCache = (Map<Long,String>) pubCache.get(keyL);
			if(maskerCache == null){
				maskerCache = new ConcurrentHashMap<Long, String>();
				pubCache.put(keyL, maskerCache);
			}
			//如果包含了当前的键值  不处理
			if(maskerCache.containsKey(keyR))
				return;
			
			//如果不包含当前键值 
			boolean hasKey = !maskerCache.isEmpty();
			if(hasKey){
				Long[] keyRSet = maskerCache.keySet().toArray(new Long[0]);
				Arrays.sort(keyRSet);
				//最大右键
				//如果有右键 取所有右键键值最大的,与当前键值做比较
				Long lastKeyR =  keyRSet[keyRSet.length -1];
				//如果当前比较旧,不做处理
				if(lastKeyR > keyR)
					return;
			}
			//如果没有右键 直接写入
			//如果当前的比较新,清除所有右键 将当前键写入
			maskerCache.clear();
			maskerCache.put(keyR, genMaskerInfoScript(md));
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(),e);
		}finally{
			// 注册key到sessionBean中
			SessionBean seb = LuiRuntimeContext.getSessionBean();
			if(seb != null){
				StringBuilder maskerKeySb = new StringBuilder();
				maskerKeySb.append(keyL).append(",").append(keyR);
				seb.setMaskerkey(maskerKeySb.toString());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void makeMaskerScript(StringBuffer sb){
		String keyL =  DEFAULTKEYLEFT;
		Long keyR = DEFAULTKEYRIGHT;
		String dsName = LuiRuntimeContext.getDatasource();
		LUICache pubCache = CacheMgr.getStrongCache(PUB_MASKER_CACHE, dsName);
		SessionBean seb = LuiRuntimeContext.getSessionBean();
		if(seb != null){
			String Maskerkey = seb.getMaskerkey();
			if(StringUtils.isNotEmpty(Maskerkey)){
				String[] keys = Maskerkey.split(",");
				keyL = keys[0];
				if(keys.length == 2)
				keyR =  Long.parseLong(keys[1]); 
			}
		}
		//未登录 无缓存  初始化默认缓存
		if(seb == null && pubCache.get(keyL) == null)
			registerMasker(null);
		
		Map<Long,String> maskerCache = (Map<Long,String>) pubCache.get(keyL);
		if(maskerCache != null && !maskerCache.isEmpty()) {
			if(maskerCache.containsKey(keyR)){
				sb.append(maskerCache.get(keyR));
				return;
			}else{
				Long[] keyRSet = maskerCache.keySet().toArray(new Long[0]);
				Arrays.sort(keyRSet);
				//最大右键
				//如果有右键 取所有右键键值最大的,与当前键值做比较
				Long lastKeyR =  keyRSet[keyRSet.length -1];
				//如果当前比较旧 
				if(lastKeyR > keyR){
					sb.append(maskerCache.get(lastKeyR));
					return;
				} 
			}
		}
		//默认
		sb.append(genMaskerInfoScript(null));
		return;
	}
	
	/**
	 * 生成Masker信息脚本
	 * @param md
	 */
	public static String genMaskerInfoScript(FormatDocVO md){
		StringBuilder sb = new StringBuilder();
		genHead(sb);
		try {
			genBody(md, sb);
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(),e);
		}
		genTail(sb);
		return sb.toString();
	}
 
	/**
	 * 生产Masker的详细信息
	 * @param md
	 * @param sb
	 */
	public static void genBody(FormatDocVO md ,StringBuilder sb){
		if(md == null || md.getPk_formatdoc() == null || md.getPk_formatdoc().equals(DEFAULTKEYLEFT))
			return;
		FormatMeta  fm =null;// md.getFm();
		if(fm == null)
			return;
		AddressFormatMeta afm = fm.getAfm() == null ? null : fm.getAfm().toNCFormatMeta();
		CurrencyFormatMeta cfm = fm.getCfm() == null ? null : fm.getCfm().toNCFormatMeta();
		TimeFormatMeta tfm = fm.getTfm() == null ? null : fm.getTfm().toNCFormatMeta();
		DateFormatMeta dfm = fm.getDfm() == null ? null : fm.getDfm().toNCFormatMeta();
		NumberFormatMeta nfm = fm.getNfm() == null ? null : fm.getNfm().toNCFormatMeta();
//		if(afm == null || cfm == null || tfm == null || dfm == null || nfm == null)
//			return;
		//地址格式化信息
		if (afm != null){
			sb.append("afm.express = '"+escape(afm.getExpress())+"';");
			sb.append("afm.separator = '"+escape(afm.getSeparator())+"';");
		}
		
		//货币格式信息
		if (cfm != null){
			sb.append("cfm.isNegRed = "+cfm.isNegRed()+";");
			sb.append("cfm.isMarkEnable = "+cfm.isMarkEnable()+";");
			sb.append("cfm.markSymbol = '"+escape(cfm.getMarkSymbol())+"';");
			sb.append("cfm.pointSymbol = '"+escape(cfm.getPointSymbol())+"';");
			sb.append("cfm.positiveFormat = '"+escape(cfm.getPositiveFormat())+"';");
			sb.append("cfm.negativeFormat = '"+escape(cfm.getNegativeFormat())+"';");
		}	
		
		//日期格式信息
		if (dfm != null){
			sb.append("dfm.format = '"+escape(dfm.getFormat())+"';");
			sb.append("dfm.speratorSymbol = '"+escape(dfm.getSperatorSymbol())+"';");
			sb.append("dtfm.format = '"+escape(dfm.getFormat())+" "+ escape(tfm.getFormat()) +"';");
			sb.append("dtfm.speratorSymbol = '"+escape(dfm.getSperatorSymbol())+"';");
		}	
		
		//时间格式信息
		if (tfm != null){
			sb.append("tfm.format = '"+escape(tfm.getFormat())+"';");
			sb.append("tfm.speratorSymbol = '"+escape(tfm.getSperatorSymbol())+"';");
		}	
		
		//数字格式信息
		if (nfm != null){
			sb.append("nfm.isNegRed = "+nfm.isNegRed()+";");
			sb.append("nfm.isMarkEnable = "+nfm.isMarkEnable()+";");
			sb.append("nfm.markSymbol = '"+escape(nfm.getMarkSymbol())+"';");
			sb.append("nfm.pointSymbol = '"+escape(nfm.getPointSymbol())+"';");
			sb.append("nfm.positiveFormat = '"+escape(nfm.getPositiveFormat())+"';");
			sb.append("nfm.negativeFormat = '"+escape(nfm.getNegativeFormat())+"';");
		}	
	}
	
	/**
	 * 生产Masker配置的头
	 * @param sb
	 */
	public static void genHead(StringBuilder sb){
		sb.append("window.$maskerMeta = {};\r\n");
		sb.append("var afm = $.addressformatmeta.getObj();\r\n");
		sb.append("var cfm = $.currencyformatmeta.getObj();\r\n");
		sb.append("var dfm = $.dateformatmeta.getObj();\r\n");
		sb.append("var tfm = $.timeformatmeta.getObj();\r\n");
		sb.append("var nfm = $.numberformatmeta.getObj();\r\n");
		sb.append("var dtfm = $.datetimeformatmeta.getObj();\r\n");
	}
	/**
	 * 生产Masker配置的尾
	 * @param sb
	 */
	public static void genTail(StringBuilder sb){
		sb.append("window.$maskerMeta.NumberFormatMeta = nfm;\r\n");
		sb.append("window.$maskerMeta.AddressFormatMeta = afm;\r\n");
		sb.append("window.$maskerMeta.CurrencyFormatMeta = cfm;\r\n");
		sb.append("window.$maskerMeta.DateFormatMeta = dfm;\r\n");
		sb.append("window.$maskerMeta.TimeFormatMeta = tfm;\r\n");
		sb.append("window.$maskerMeta.DateTimeFormatMeta = dtfm;\r\n");
	}
	
	/**
	 * 对字符串进行转义
	 * @param str
	 * @return
	 */
	private static String escape(String str){
		return str.replace("'", "\'").replace("\"\"", "");
	}
}
