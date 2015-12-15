package xap.lui.psn.cmd;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.model.LuiAppUtil;

public class LuiFormLoadCmd extends LuiCommand {
	private String OperatorDs;
	
	
	public String getOperDsId() {
		return OperatorDs;
	}

	public LuiFormLoadCmd(String operDsId){
		this.OperatorDs=operDsId;
	}


	public void execute() {
		 String operDsId=(String)LuiAppUtil.getAppAttr("dsId");
		 this.OperatorDs=StringUtils.isBlank(operDsId)?this.OperatorDs:operDsId;
		 Dataset ds=LuiAppUtil.getDataset(this.OperatorDs);
		 Row row=(Row)LuiAppUtil.getAppAttr("selRow");
		 row=row==null?ds.getEmptyRow():row;
		 ds.insertRow(0, row);
		 ds.setEdit(true);
		 ds.setSelectedIndex(0);
	}
	

}
