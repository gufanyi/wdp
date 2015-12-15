package lui.ctrl;
import xap.lui.core.command.CmdInvoker;
import java.util.Map;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.sys.xbd.udi.d.UdidoclistDO;
import xap.lui.exta.qrytmpl.qryscheme.xml.QueryNode;
import xap.lui.exta.qrysql.QueryMetaCmd;
import xap.mw.core.data.BaseDO;
import xap.lui.core.dataset.Dataset;

public class MethodTemplate {

	<#if OperatorDs?exists>
		public void pluginsimpleQuery_plugin(Map<Object, Object> keys){
			String dsId = "${OperatorDs}";
			Dataset ds_bd_udidoclist=LuiAppUtil.getCntWindowCtx().getViewContext("main").getView().getViewModels().getDataset(dsId);
			UdidoclistDO[] scapts = new	UdidoclistDO[0];
			clearList(ds_bd_udidoclist,scapts);
			QueryNode root=(QueryNode) keys.get("QueryNode");
			String classname=(String) keys.get("classname");
			QueryMetaCmd qmc=new QueryMetaCmd();
			qmc.DSLoadCmd("main", dsId, root, classname);
	
		}
	<#else>
		private void clearList(Dataset gridDsList,BaseDO[] Dos){
			gridDsList.clear();
			new SuperVO2DatasetSerializer().serialize(Dos, gridDsList);
		}
	</#if>
		
}
