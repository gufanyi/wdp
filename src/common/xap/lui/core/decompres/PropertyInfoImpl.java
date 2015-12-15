package xap.lui.core.decompres;



public class PropertyInfoImpl implements IFormResoureProvider.IPropertyInfo
{
  private String fName;
  private int fType;
  private String fValue;

  public PropertyInfoImpl(String aName, int aType, String aValue)
  {
    this.fName = aName;
    this.fType = aType;
    this.fValue = aValue;
  }

  public String getPropertyName() {
    return this.fName;
  }
  public int getPropertyType() {
    return this.fType;
  }
  public String getPropertyValue() {
    return this.fValue;
  }

  public String toString() {
    return   "Name, " + this.fName + "; Type, " + Integer.valueOf(this.fType) + "; Value," + this.fValue ;
  }
}
