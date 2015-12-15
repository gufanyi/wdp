
package xap.lui.core.format;

import java.io.Serializable;



public class AddressObjectAdaptor implements AddressObject, Serializable {

	
	private static final long serialVersionUID = -830426749008826981L;
	
	
	private AddressVO addressVO;
	
	/* (non-Javadoc)
	 */
	@Override
	public String getCity() {
		if(addressVO != null)
		{
			return addressVO.getCity();
		}
		return "������";
	}

	/* (non-Javadoc)
	 */
	@Override
	public String getCountry() {
		if(addressVO != null)
		{
			addressVO.getCountry();
		}
		return "�й�";
	}

	/* (non-Javadoc)
	 */
	@Override
	public String getPostcode() {
		if(addressVO != null)
		{
			return addressVO.getPostcode();
		}
		return "100094";
	}

	/* (non-Javadoc)
	 */
	@Override
	public String getRoad() {
		if(addressVO != null)
		{
			addressVO.getDetailinfo();
		}
		return "";
	}

	/* (non-Javadoc)
	 */
	@Override
	public String getState() {
		if(addressVO != null)
		{
			addressVO.getProvince();
		}
		return "����";
	}

	
	public AddressVO getAddressVO() {
		return addressVO;
	}

	
	public void setAddressVO(AddressVO addressVO) {
		this.addressVO = addressVO;
	}

	@Override
	public String getSection() {
		if(addressVO != null)
			addressVO.getVsection();
		
		return "������";
	}

}
