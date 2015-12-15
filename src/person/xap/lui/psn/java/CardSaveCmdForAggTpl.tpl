public class MethodTemplate {

   <#if OperatorDs?exists && NavDsId?exists &&CardLayoutId?exists>
   public void LuiCardSaveDOCmd() {
		String dsId = "${OperatorDs}";
		String cardLayoutId = "${CardLayoutId}";
		String navDsId = "${NavDsId}";
		CmdInvoker.invoke(new LuiCardSaveDOCmd(dsId,cardLayoutId,navDsId));
	}
   <#else>	
	public void LuiCardSaveDOCmd() {
		String dsId = "${OperatorDs}";
		String cardLayoutId = "${CardLayoutId}";
		CmdInvoker.invoke(new LuiCardSaveDOCmd(dsId,cardLayoutId));
	}
    </#if>	
}