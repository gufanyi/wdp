package xap.lui.core.decompres;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;


public class DecompressionRuntime {
	// public ApplicationContext fbeanFactory;
	private static DecompressionRuntime fInstance = new DecompressionRuntime();
	// private IDbAdapter fDbAdapter;
	protected boolean isInited = false;
	// private ISeeyonFormAppManager fAppManager;
	// private final String C_sBean_Dbadapter = "form_dbadapter";
	// private final String C_sBean_AppManager = "form_appmanager";
	// private final String C_sBean_EnumManager = "form_systemenummanager";
	// private SeeyonFormHBConfiguration config = null;
	// private SessionFactory sessionfactory = null;
	// private SystemProperties sys = null;
	// private static final TLogUtils log = new
	// TLogUtils(SeeyonForm_Runtime.class);
	//
	// private final String C_sFile_I18n =
	// "www.seeyon.com.v3x.form.resources.i18n.FormResources";
	// private IFormEnumManager fSystemEnumManager;
	protected RuntimeCharset fRunCharset;

	// protected DatatypeChange fDataType;
	// protected EnumtypeChange fEnumChange;
	// private final String C_sBean_SystemValueManager =
	// "form_systemvaluemanager";
	//
	// private final String C_sBean_SystemFunctionManager =
	// "from_systemfunctionmanager";
	//
	// private final String C_sBean_InputExtendManager =
	// "from_inputextendmanager";
	//
	// private final String C_sBean_ViewEventManager = "from_vieweventmanager";
	//
	// private final String C_sBean_PoCheckManager = "form_pocheckmanager";
	//
	// public SystemProperties getSys()
	// {
	// if (this.sys == null)
	// this.sys = SystemProperties.getInstance();
	// return this.sys;
	// }

	protected synchronized void init() {
		if (fInstance.isInited) {
			return;
		}
		this.isInited = true;

		if (this.fRunCharset == null)
			this.fRunCharset = new RuntimeCharset("UTF-8", "UTF-8", "UTF-8",
					"UTF-8", "UTF-8");
		// this.fDataType = new DatatypeChange();
		// this.fEnumChange = new EnumtypeChange();
		// String[] paths = { "hibernate_formtest.cfg.xml", "form-manager.xml"
		// };
		// this.fbeanFactory = new ClassPathXmlApplicationContext(paths);
		// this.fDbAdapter = ((IDbAdapter)getBean("form_dbadapter"));
		// this.fAppManager =
		// ((ISeeyonFormAppManager)getBean("form_appmanager"));
	}

	public static DecompressionRuntime getInstance() {
		if (!fInstance.isInited)
			fInstance.init();
		return fInstance;
	}

	// public final IDbAdapter getDBAdapter()
	// {
	// return this.fDbAdapter;
	// }
	//
	// public final ISeeyonFormAppManager getAppManager()
	// {
	// return this.fAppManager;
	// }

	protected Locale getCurrentLocale() {
		return Locale.SIMPLIFIED_CHINESE;
	}

	// public Object getBean(String beanName)
	// {
	// return this.fbeanFactory.getBean(beanName);
	// }
	//
	// public SessionFactory returnSessionFactory()
	// {
	// return this.sessionfactory;
	// }
	// private SessionFactory getSessionFactory() {
	// if (this.sessionfactory == null) {
	// this.sessionfactory = this.config.buildSessionFactory();
	// }
	// return this.sessionfactory;
	// }
	//
	// public SeeyonFormHBConfiguration getConfiguration() {
	// if (this.config == null) {
	// this.config =
	// getInstance().newFormConfiguration();
	// }
	// return this.config;
	// }

	// public final String getI18Str(String aKey, Object[] parameters)
	// {
	// return
	// ResourceBundleUtil.getString("www.seeyon.com.v3x.form.resources.i18n.FormResources",getCurrentLocale(),
	// aKey, parameters);
	// }

	// public SeeyonFormHBConfiguration newFormConfiguration()
	// {
	// SeeyonFormHBConfiguration result = new SeeyonFormHBConfiguration();
	// Properties fcfgPts = result.getProperties();
	//
	// SystemProperties sys = SystemProperties.getInstance();
	// fcfgPts.put("hibernate.dialect", sys.getProperty("db.hibernateDialect"));
	// fcfgPts.put("hibernate.connection.datasource",
	// sys.getProperty("db.jndiName"));
	//
	// return result;
	// }

	/**
	 * 将xsn表单模板解析为字节数组
	 */
	//public byte[] readResourceData(String aResourceName) {
	public byte[] readResourceData(InputStream fin) {
		//InputStream fin = null;
		try {
			// System.out.println(this.getClass().getResource(aResourceName));
			//fin = this.getClass().getResourceAsStream(aResourceName);
			if (null == fin)
				return null;

			byte[] ftemp = new byte[8192];
			int flen = 0;
			ByteArrayOutputStream fbyteout = new ByteArrayOutputStream(8192);
			while (flen >= 0) {
				flen = fin.read(ftemp);
				if (flen > 0) {
					fbyteout.write(ftemp, 0, flen);
				}
			}
			ftemp = fbyteout.toByteArray();
			byte[] arrayOfByte1 = ftemp;
			return arrayOfByte1;
		} catch (Exception ex) {
			LuiLogger.error(ex.getMessage(), ex);
		} finally {
			try {
				if (fin != null)
					fin.close();
			} catch (Exception localException3) {
				LuiLogger.error(localException3.getMessage(), localException3);
			}
		}
		return null;
	}
	

	//
	// public String readResource(String aResourceName) {
	// byte[] result = readResourceData(aResourceName);
	// if (result == null) {
	// return "";
	// }
	// return new String(result);
	// }
	//
	// public String readResourceSelfXML(String aResourceName) {
	// byte[] result = readResourceData(aResourceName);
	// if (result == null) {
	// return "";
	// }
	// return this.fRunCharset.SystemDefault2SelfXML_Byte(result);
	// }
	//
	// public String readResourceSystemDefault(String aResourceName) {
	// byte[] result = readResourceData(aResourceName);
	// if (result == null) {
	// return "";
	// }
	// return this.fRunCharset.getSystemDefault(result);
	// }

	// public IFormEnumManager getSystemEnumManager()
	// {
	// this.fSystemEnumManager =
	// ((IFormEnumManager)getBean("form_systemenummanager"));
	// return this.fSystemEnumManager;
	// }
	//
	public RuntimeCharset getCharset() {
		return this.fRunCharset;
	}
	//
	// public DatatypeChange getChange()
	// {
	// return this.fDataType;
	// }
	//
	// public EnumtypeChange getEnumChange()
	// {
	// return this.fEnumChange;
	// }
	//
	// public ISystemValueManager getSystemValueManager()
	// {
	// return (ISystemValueManager)getBean("form_systemvaluemanager");
	// }
	//
	// public ISystemFunctionManager getSystemFunctionManager()
	// {
	// return (ISystemFunctionManager)getBean("from_systemfunctionmanager");
	// }
	//
	// public IInputExtendManager getInputExtendManager()
	// {
	// return (IInputExtendManager)getBean("from_inputextendmanager");
	// }
	//
	// public IViewEventManager getViewEventManager()
	// {
	// return (IViewEventManager)getBean("from_vieweventmanager");
	// }
	//
	// public PoCheckManager getPoCheckManager()
	// {
	// return (PoCheckManager)getBean("form_pocheckmanager");
	// }
}
