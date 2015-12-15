package xap.lui.core.decompres;

import java.io.ByteArrayOutputStream;


public class ResourceInfoImpl implements IFormResoureProvider.IResourceInfo {
	private String fResourceName;
	private ByteArrayOutputStream fResourceInfo;

	public ByteArrayOutputStream getByteArrayOutputStream() {
		return this.fResourceInfo;
	}

	public void setByteArrayOutputStream(ByteArrayOutputStream aStream) {
		this.fResourceInfo = aStream;
	}

	public String getResourceInfo() {
		return new String(this.fResourceInfo.toByteArray());
	}

	public String getResourceName() {
		return this.fResourceName;
	}

	public ResourceInfoImpl(String aName, ByteArrayOutputStream aInfo) {
		this.fResourceName = aName;
		this.fResourceInfo = aInfo;
	}
}
