package xap.lui.core.decompres;

public class RuntimeCharset {
	private String C_sCharset_SystemDefault;
	private String C_sCharset_JspServlet;
	private String C_sCharset_JDK;
	private String C_sCharset_JDKFile;
	private String C_sCharset_DBIn;
	private String C_sCharset_DBOut;
	private String C_sCharset_SelfXML;

	public RuntimeCharset(String aSystem, String aDbin, String aDbOut,
			String aJDK, String aSalfXML) {
		this.C_sCharset_SystemDefault = aSystem;

		this.C_sCharset_DBIn = aDbin;
		this.C_sCharset_DBOut = aDbOut;
		this.C_sCharset_JspServlet = this.C_sCharset_SystemDefault;
		this.C_sCharset_JDK = aJDK;
		this.C_sCharset_JDKFile = aJDK;
		this.C_sCharset_SelfXML = aSalfXML;
	}

	public String getSystemDefaultCharset() {
		return this.C_sCharset_SystemDefault;
	}

	public String getSelfXML() {
		return this.C_sCharset_SelfXML;
	}

	private boolean checkNoConvert(String aStr, String aSourceCharset,
			String aDescCharset) {
		if (aSourceCharset.equals(aDescCharset))
			return true;

		return (aStr == null) || (aStr.equals(""));
	}

	private String convertCharSet(String aStr, String aSourceCharset,
			String aDescCharset) {
		if (checkNoConvert(aStr, aSourceCharset, aDescCharset))
			return aStr;
		try {
			return new String(aStr.getBytes(aSourceCharset), aDescCharset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String convertCharSet_1(String aStr, String aSourceCharset,
			String aDescCharset) {
		if (checkNoConvert(aStr, aSourceCharset, aDescCharset))
			return aStr;
		try {
			return new String(aStr.getBytes(aDescCharset));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String convertCharSet_2(String aStr, String aSourceCharset,
			String aDescCharset) {
		if (checkNoConvert(aStr, aSourceCharset, aDescCharset))
			return aStr;
		try {
			return new String(aStr.getBytes(aDescCharset), aSourceCharset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private String convertCharSet_3(String aStr, String aSourceCharset,
			String aDescCharset) {
		if (checkNoConvert(aStr, aSourceCharset, aDescCharset))
			return aStr;
		try {
			return new String(aStr.getBytes(), aDescCharset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String convertCharSet_4(String aStr, String aSourceCharset,
			String aDescCharset) {
		if (checkNoConvert(aStr, aSourceCharset, aDescCharset))
			return aStr;
		try {
			String ftemp = new String(aStr.getBytes(aDescCharset));
			return ftemp;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String JDK2DBIn(String aStr) {
		return convertCharSet(aStr, this.C_sCharset_JDK, this.C_sCharset_DBIn);
	}

	public String JDK2SelfXML(String aStr) {
		return convertCharSet(aStr, this.C_sCharset_JDK,
				this.C_sCharset_SelfXML);
	}

	public String SystemDefault2JDK(String aStr) {
		return convertCharSet_3(aStr, this.C_sCharset_SystemDefault,
				this.C_sCharset_JDK);
	}

	public String SystemDefault2SelfXML(String aStr) {
		return convertCharSet_3(aStr, this.C_sCharset_SystemDefault,
				this.C_sCharset_SelfXML);
	}

	public String SelfXML2SystemDefault(String aStr) {
		return convertCharSet_4(aStr, this.C_sCharset_SelfXML,
				this.C_sCharset_SystemDefault);
	}

	public String SelfXML2JDK(String aStr) {
		return convertCharSet(aStr, this.C_sCharset_JDK,
				this.C_sCharset_SelfXML);
	}

	public String DBOut2JDK(String aStr) {
		return convertCharSet(aStr, this.C_sCharset_DBOut, this.C_sCharset_JDK);
	}

	public String DBOut2SelfXML(String aStr) {
		return convertCharSet(aStr, this.C_sCharset_DBOut,
				this.C_sCharset_SelfXML);
	}

	public String SystemDefault2SelfXML_Byte(byte[] aBytes) {
		if (aBytes == null)
			return null;
		if (aBytes.length <= 0)
			return "";
		try {
			String ftemp = new String(aBytes, this.C_sCharset_SystemDefault);
			ftemp = new String(ftemp.getBytes(), this.C_sCharset_SelfXML);
			return ftemp;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getSystemDefault(byte[] aBytes) {
		try {
			String ftemp = new String(aBytes, this.C_sCharset_SystemDefault);
			return ftemp;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String SystemDefault2Dbin(String aStr) {
		if (checkNoConvert(aStr, this.C_sCharset_SystemDefault,
				this.C_sCharset_DBIn))
			return aStr;
		try {
			String ftemp = new String(aStr.getBytes(), this.C_sCharset_DBIn);
			return ftemp;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String SystemDefault2JspServlet(String aStr) {
		return convertCharSet(aStr, this.C_sCharset_SystemDefault,
				this.C_sCharset_JspServlet);
	}

	public String JDK2JspServlet(String aStr) {
		if (checkNoConvert(aStr, this.C_sCharset_JDK,
				this.C_sCharset_JspServlet))
			return aStr;
		try {
			String ftemp = new String(aStr.getBytes(this.C_sCharset_JspServlet));
			return ftemp;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String JspServlet2JDK(String aStr) {
		if (checkNoConvert(aStr, this.C_sCharset_JspServlet,
				this.C_sCharset_JDK))
			return aStr;
		try {
			String ftemp = new String(aStr.getBytes(), this.C_sCharset_JDK);
			return ftemp;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
