package xap.lui.core.decompres;

import java.util.List;


public abstract interface IFormResoureProvider {
	public abstract void addFormProperty(String paramString1, int paramInt,
			String paramString2);

	public abstract List<IPropertyInfo> getFormPropertyList();

	public abstract String loadResource(String paramString);

	public abstract byte[] loadResourcetoByte(String paramString);

	public abstract void addResource(String paramString1, String paramString2)
			throws LuiInfoPathParserException;

	public abstract List<IResourceInfo> getResourceList();

	public abstract String getPropertyValue(String paramString, int paramInt);

	public static abstract interface IPropertyInfo {
		public abstract String getPropertyName();

		public abstract int getPropertyType();

		public abstract String getPropertyValue();
	}

	public static abstract interface IResourceInfo {
		public abstract String getResourceName();

		public abstract String getResourceInfo();
	}
}
