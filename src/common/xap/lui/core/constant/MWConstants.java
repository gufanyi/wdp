package xap.lui.core.constant;
public class MWConstants {
	public static final String PropXmlPath = "./config/appserver/middleware_config.xml";
	public static final String IndustryConfigPath = "/config/appserver/industry_config.xml";
	public static final String FileGroups = "config/appserver/file_asyn_config.xml";
	public static final String LogConfigPath = "/config/appserver/logger_server_config.properties";
	public static final String LoggerConfigPath = "/config/appserver/logger_wdp_config.properties";
	public static final String DocumentcenterConfigPath = "/config/appserver/documentcenter-config.properties";
	public static final String RESTRICTION_FILE = "/config/appserver/task_restrictions.xml";
	public static final String dbdriverset = "/config/appserver/db_driver_set.xml";
	public static final String CaProp = "/config/appserver/ca_config.xml";
	public static final String PassWordConfig = "/config/appserver/password_config.xml";
	public static final String SysConfigPath = "config/appserver/login_config.xml";
	public static final String URL_IPCONF = getWDPHome() + "/config/appserver/ipbound_config.conf";
	public static final String CONFIG_FILE_SCHE_ENG = "schedule_config.xml";
	public static final String HaiYouServerLocation = "haiyou.server.location";
	public static final String HaiYouServerHome = "haiyou.server.home";
	public static final String HaiYouServerName = "haiyou.server.name";
	public static final String HaiYouStartCount = "haiyou.server.startCount";
	public static final String HaiYouServerProp = "haiyou.server.prop";
	public static final String HaiYouServerMode = "haiyou.server.mode";
	public static final String HaiYouServerDomain = "haiyou.server.withDaemon";
	public static final String HaiYouServerHa = "haiyou.server.ha";
	public static final String HaiYouRunSide = "haiyou.run.side";
	public static final String HaiYouRunMode = "haiyou.runMode";
	public static String getWDPHome() {
		String wdpHome = System.getProperty(MWConstants.HaiYouServerLocation, System.getProperty("user.dir"));
		return wdpHome;
	}
	public static String getHost() {
		String host = "localhost";
		return host;
	}
	public static String getPort() {
//		ServerProcessInfo serverInfo = Bootstrap.getServerProcessInfo(new File(getWDPHome()), getServerName());
//		ProtocolInfo pi = serverInfo == null ? null : serverInfo.getHttpProtocol();
//		String port = System.getProperty("haiyou.http.port", pi == null ? "80" : "" + pi.getPort());
		return null;
	}
	public static String getServerName() {
		String serverName = System.getProperty(MWConstants.HaiYouServerName);
		if (serverName == null) {
			serverName = "server";
		}
		return serverName;
	}
}
