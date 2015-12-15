/**
 * 
 */
package xap.lui.psn.context;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public abstract class BaseInfo implements IBaseInfo {
	private static final long serialVersionUID = -3659134536127052351L;
	
	public List<IPropertyInfo> list = new ArrayList<IPropertyInfo>();
	
	public String[] getColorKeys(){
		return new String[]{
				"默认",
				"黑", 
				"红", 
				"绿",
				"蓝", 
				"灰",
				"白" 
		};
	}
	
	public String[] getColorValues(){
		return new String[]{
				"",
				"black",
				"red",
				"green",
				"blue",
				"gray",
				"white"
		};
	}
	
	
	public String[] getBorderStyleKeys(){
		return new String[]{
				"实线","双线","虚线","点状线"
		};
	}
	
	public String[] getBorderStyleValues(){
		return new String[]{
				"solid",
				"double",
				"dashed",
				"dotted"
		};
	}
	
	
	
	public BaseInfo(){
		super();
		StringPropertyInfo sinfo = new StringPropertyInfo();
		sinfo.setId("id");
		sinfo.setVisible(true);
		sinfo.setEditable(false);
		sinfo.setDsField("string_ext1");
		sinfo.setVoField("id");
		sinfo.setLabel("ID");
		list.add(sinfo);
	}
	
	public IPropertyInfo[] getPropertyInfos(){
		return list.toArray(new IPropertyInfo[0]);
	}
	
	public void removePropertyInfoById(String id){
		if(id==null || StringUtils.isBlank(id)) return;
		for (IPropertyInfo info : list) {
			if(info==null) return;
			if(id.equals(info.getId())){
				list.remove(info);
				break;
			}
		}
	}
}
