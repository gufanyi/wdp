package xap.lui.core.decompres;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class CabFileResourceProvider implements IFormResoureProvider {
	private HashMap<String, ResourceInfoImpl> ffileList = new HashMap<String, ResourceInfoImpl>(10);

	private HashMap<String, PropertyInfoImpl> fpropertyList = new HashMap<String, PropertyInfoImpl>(10);

	public CabFileResourceProvider(InputStream aInputstream) throws LuiInfoPathParserException {
		prvExtractCabDecoder fdecoder = new prvExtractCabDecoder();
		try {
			CabDecoder fzip = new CabDecoder(aInputstream, fdecoder);
			fzip.extract();
			convertCabFileCharset();
		} catch (Exception e) {
			DataDefineException ex1 = new DataDefineException(1020, e);
			if (e.getMessage().indexOf("Missing header signature") > -1){
//				ex1.setToUserMsg(SeeyonForm_Runtime.getInstance().getI18Str(
//						"DataDefine.formatError", new Object[0]));
				ex1.setToUserMsg("DataDefine.formatError");
			}else {
//				ex1.setToUserMsg(SeeyonForm_Runtime.getInstance().getI18Str(
//						"DataDefine.ParseError", new Object[0]));
				ex1.setToUserMsg("DataDefine.ParseError");
			}

			throw ex1;
		}
	}

	private void convertCabFileCharset() throws LuiInfoPathParserException {
		RuntimeCharset fCharset = DecompressionRuntime.getInstance().getCharset();

		//resourceName没有拿到？
		for (ResourceInfoImpl fitem : this.ffileList.values()) {
			byte[] ftempByte;
			if (fitem.getResourceName().endsWith(".xsl"))
				ftempByte = CharReplace.doReplace_Encode(fitem
						.getByteArrayOutputStream().toByteArray());
			else {
				ftempByte = fitem.getByteArrayOutputStream().toByteArray();
			}
			if ((fitem.getResourceName().endsWith(".xsl"))
					|| (fitem.getResourceName().endsWith(".xsd"))
					|| (fitem.getResourceName().endsWith(".xsf"))
					|| (fitem.getResourceName().endsWith(".xml"))
					|| (fitem.getResourceName().endsWith(".js"))) {
				String ftemp = fCharset.getSystemDefault(ftempByte);
				ftempByte = ftemp.getBytes();
			}
			ByteArrayOutputStream fout = new ByteArrayOutputStream(300);
			try {
				fout.write(ftempByte);
			} catch (Exception e) {
				DataDefineException ex1 = new DataDefineException(1022, e);

//				ex1.setToUserMsg(SeeyonForm_Runtime.getInstance().getI18Str(
//						"DataDefine.NotConnectDB", new Object[0]));
				ex1.setToUserMsg("DataDefine.NotConnectDB");
				throw ex1;
			}

			fitem.setByteArrayOutputStream(fout);
		}
	}

	public String loadResource(String aResourceName) {
		ResourceInfoImpl ftemp = (ResourceInfoImpl) this.ffileList.get(aResourceName);
		if (ftemp == null)
			return null;
		return new String(ftemp.getByteArrayOutputStream().toByteArray());
	}

	public byte[] loadResourcetoByte(String aResourceName) {
		ResourceInfoImpl ftemp = (ResourceInfoImpl) this.ffileList
				.get(aResourceName);
		if (ftemp == null)
			return null;
		return ftemp.getByteArrayOutputStream().toByteArray();
	}

	public void addResource(String aResourceName, String aResourceInfo)
			throws LuiInfoPathParserException {
		ByteArrayOutputStream fbuffer = new ByteArrayOutputStream(300);
		try {
			fbuffer.write(aResourceInfo.getBytes());
		} catch (Exception e) {
			DataDefineException ex1 = new DataDefineException(1022, e);
//			ex1.setToUserMsg(SeeyonForm_Runtime.getInstance().getI18Str(
//					"DataDefine.WriteAbnormity", new Object[0]));
			ex1.setToUserMsg("DataDefine.WriteAbnormity");
			throw ex1;
		}

		this.ffileList.put(aResourceName, new ResourceInfoImpl(aResourceName,
				fbuffer));
	}

	public List<IFormResoureProvider.IResourceInfo> getResourceList() {
		List result = new ArrayList();
		result.addAll(this.ffileList.values());
		return result;
	}

	public void addFormProperty(String aName, int aType, String aValue) {
		this.fpropertyList.put(aType + "_" + aName, new PropertyInfoImpl(aName,
				aType, aValue));
	}

	public List<IFormResoureProvider.IPropertyInfo> getFormPropertyList() {
		List result = new ArrayList(10);
		result.addAll(this.fpropertyList.values());
		return result;
	}

	public String getPropertyValue(String aName, int aType) {
		IFormResoureProvider.IPropertyInfo ftemp = (IFormResoureProvider.IPropertyInfo) this.fpropertyList
				.get(aType + "_" + aName);
		if (ftemp == null)
			return null;
		return ftemp.getPropertyValue();
	}

	private class prvExtractCabDecoder implements CabDecoderInterface {
		private prvExtractCabDecoder() {
		}

		public boolean closeOutputStream(OutputStream outputstream,
				CabFileEntry cabfileentry, boolean flag) {
			ResourceInfoImpl ftemp = (ResourceInfoImpl) CabFileResourceProvider.this.ffileList
					.get(cabfileentry.getName());
			try {
				if (ftemp != null)
					ftemp.getByteArrayOutputStream().close();
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		public InputStream openCabinet(String s, String s1) {
			return null;
		}

		//把CabFileEntry放在map中
		public OutputStream openOutputStream(CabFileEntry cabfileentry) {
			ResourceInfoImpl ftemp = new ResourceInfoImpl(cabfileentry.getName(), new ByteArrayOutputStream(300));
			CabFileResourceProvider.this.ffileList.put(cabfileentry.getName(), ftemp);
			return ftemp.getByteArrayOutputStream();
		}

		public boolean reservedAreaData(int i, byte[] abyte0, int j,
				byte[] abyte1, int k) {
			return false;
		}

		public Object progress(int i, long l, long l1, Object[] aobj) {
			return null;
		}
	}
}
