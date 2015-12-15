package xap.lui.core.dataset;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.alibaba.fastjson.annotation.JSONField;
import xap.lui.core.exception.LuiRuntimeException;
public class ParameterSet implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	private List<Parameter> paramList = new Vector<Parameter>();
	public void addParameter(Parameter param) {
		if (paramList != null && paramList.size() > 0 && paramList.contains(param)) {
			int index = paramList.indexOf(param);
			paramList.get(index).setValue(param.getValue());
		} else {
			paramList.add(param);
		}
	}
	public void addParameters(List<Parameter> paramList) {
		for (Parameter param : paramList)
			this.addParameter(param);
	}
	public void removeParameter(Parameter param) {
		paramList.remove(param);
	}
	public void removeParameter(String paramName) {
		for (Parameter param : paramList) {
			if (param.getName().trim().equals(paramName)) {
				paramList.remove(param);
				break;
			}
		}
	}
	public Parameter getParameter(String paramName) {
		for (Parameter param : paramList) {
			if (param.getName().trim().equals(paramName))
				return param;
		}
		return null;
	}
	public String getParameterValue(String paramName) {
		for (Parameter param : paramList) {
			if (param.getName().trim().equals(paramName))
				return param.getValue();
		}
		return null;
	}
	@JSONField(serialize = true)
	public Parameter[] getParameters() {
		return (Parameter[]) paramList.toArray(new Parameter[paramList.size()]);
	}
	public void clear() {
		this.paramList.clear();
	}
	public int size() {
		return paramList.size();
	}
	public Parameter getParameter(int index) {
		return paramList.get(index);
	}
	public Object clone() {
		try {
			ParameterSet ps = (ParameterSet) super.clone();
			ps.paramList = new ArrayList<Parameter>();
			for (Parameter param : paramList) {
				ps.paramList.add((Parameter) param.clone());
			}
			return ps;
		} catch (CloneNotSupportedException e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
}
