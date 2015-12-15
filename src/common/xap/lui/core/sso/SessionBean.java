package xap.lui.core.sso;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import xap.lui.core.common.ExtAttriSupport;
import xap.lui.core.common.LuiRuntimeContext;
import xap.mw.coreitf.d.FDate;
public abstract class SessionBean extends ExtAttriSupport implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FEATURE_CLIENT_PRINT = "FEATURE_CLIENT_PRINT";
	public static final String FEATURE_CLIENT_CACHE = "FEATURE_CLIENT_CACHE";
	public static final String CLIENT_PRINTERS = "client_printers";
	/**
	 * 用于记录客户端是否支持某种特性,比如说客户端缓存
	 */
	private Map<String, Boolean> clientSupportMap;
	/**
	 * 单点登录口令
	 */
	private String ssotoken;
	private byte[] securityToken;
	private TimeZone timeZone;
	public byte getSysid() {
		return sysid;
	}
	public void setSysid(byte sysid) {
		this.sysid = sysid;
	}
	/**
	 * 客户端打印机列表
	 */
	private String[] printers;
	private byte sysid;
	/**
	 * masker缓存键 pk_formatdoc + ts
	 */
	private String maskerkey;
	public abstract FDate getLoginDate();
	public abstract String getPk_user();
	public abstract String getUser_code();
	public abstract String getUser_name();
	public abstract Integer getUser_type();
	public abstract String getPk_unit();
	public abstract String getThemeId();
	public abstract String getLangId();
	public abstract String getDatasource();
	/**
	 * 获得单位编码
	 * 
	 * @return
	 */
	public abstract String getUnitNo();
	/**
	 * 获取单位名称
	 * 
	 * @return
	 */
	public abstract String getUnitName();
	public String[] getClientPrinters() {
		return printers;
	}
	public void setFeatureSupport(String feature, Boolean support) {
		if (clientSupportMap == null) {
			clientSupportMap = new HashMap<String, Boolean>();
		}
		clientSupportMap.put(feature, support);
	}
	public boolean isFeatureSupport(String feature) {
		if (clientSupportMap == null)
			return false;
		Boolean support = clientSupportMap.get(feature);
		return support != null && support.booleanValue();
	}
	/**
	 * 获得单点登陆凭据
	 * 
	 * @param serverIp
	 *            单点服务器IP
	 * @return
	 */
	public String getSsotoken(String serverIp) {
		return serverIp + "_" + ssotoken + "_" + getDatasource().replace("_", "~");
	}
	/**
	 * 获得单点登陆凭据
	 * 
	 * @return
	 */
	public String getSsotoken() {
		String serverIp = LuiRuntimeContext.getServerAddr();
		if (serverIp == null || serverIp.equals("")) {
			serverIp = LuiRuntimeContext.getWebContext().getRequest().getServerName();
			int port = LuiRuntimeContext.getWebContext().getRequest().getServerPort();
			if (port != 80)
				serverIp = serverIp + ":" + port;
		}
		String ssotoken = getDatasource()==null?"":(getDatasource().replace("_", "~"));
		return serverIp + "_" + ssotoken + "_" + ssotoken;
	}
	public void setSsotoken(String ssotoken) {
		this.ssotoken = ssotoken;
	}
	public void setClientPrinters(String[] printers) {
		this.printers = printers;
	}
	/**
	 * 更新本地信息，有些系统并不是从LuiRuntimeEnvironment中取环境变量，这种情况需要将变量映射到具体环境中。
	 * 本方法每个非资源请求都会调用
	 */
	public void fireLocalEnvironment() {}
	public String getMaskerkey() {
		return maskerkey;
	}
	public void setMaskerkey(String maskerkey) {
		this.maskerkey = maskerkey;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	public byte[] getSecurityToken() {
		return securityToken;
	}
	public void setSecurityToken(byte[] securityToken) {
		this.securityToken = securityToken;
	}
}
