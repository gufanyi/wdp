package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import com.alibaba.fastjson.JSON;

import xap.lui.core.echar.Option;


public class ChartBaseComp extends WebComp {
	
	

	private static final long serialVersionUID = 4177302075991369709L;

	@XmlElement(name = "Prop")
	@XmlCDATA
	private String prop = null;

	private Option option = null;

	public String getProp() {
		//if(option==null){
			this.option=this.getDftOption();
		//}
		this.prop = JSON.toJSONString(option);
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	public Option getOption() {
		return option;
	}

	public void setOption(Option option) {
		this.option = option;
	}

	public Option getDftOption() {
		option=this.getDemoOption0();
		return option;
	}

	public Option getDemoOption0() {
		return null;
	}
	
	public void updateOption(){
	}

}
