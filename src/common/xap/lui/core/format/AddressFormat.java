package xap.lui.core.format;

import xap.lui.core.vos.IElement;

public class AddressFormat extends AbstractSplitFormat {

	protected AddressFormatMeta formatMeta = null;

	public AddressFormat(AddressFormatMeta formatMeta) {
		this.formatMeta = formatMeta;
	}

	@Override
	protected String getExpress() {
		return formatMeta.getExpress();
	}

	@Override
	protected String[] getReplaceds() {
		return new String[] { formatMeta.getSeparator() };
	}

	@Override
	protected String[] getSeperators() {
		return new String[] { "(\\s)+?" };
	}

	@Override
	protected IElement getVarElement(String express) {
		if (express.equals("C"))
			return new IElement() {

				public String getValue(Object obj) {
					return ((AddressObject) obj).getCountry();
				}

			};

		if (express.equals("S"))
			return new IElement() {

				public String getValue(Object obj) {
					return ((AddressObject) obj).getState();
				}

			};

		if (express.equals("T"))
			return new IElement() {

				public String getValue(Object obj) {
					return ((AddressObject) obj).getCity();
				}

			};

		if (express.equals("D"))
			return new IElement() {

				@Override
				public String getValue(Object obj) {
					return ((AddressObject) obj).getSection();
				}

			};

		if (express.equals("R"))
			return new IElement() {

				public String getValue(Object obj) {
					return ((AddressObject) obj).getRoad();
				}

			};

		if (express.equals("P"))
			return new IElement() {

				public String getValue(Object obj) {
					return ((AddressObject) obj).getPostcode();
				}

			};

		return new StringElement(express);
	}

	@Override
	protected Object formatArgument(Object obj) throws FormatException {
		if (obj instanceof AddressObject)
			return obj;

		throw new FormatException("��֧�ָ�ʽ���ĵ�ַ��������");
	}
}
