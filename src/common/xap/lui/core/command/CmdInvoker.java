package xap.lui.core.command;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;

/**
 * 命令触发器，提供了命令批触发的多种场景
 *
 */
public final class CmdInvoker {
	/**
	 * 单一命令触发 
	 * @param cmd
	 */
	public static void invoke(ICommand cmd){
		if(cmd != null)
		cmd.execute();
	}
	
	/**
	 * 命令批触发
	 * @param cmds
	 * @param breakCmd 是否中断
	 */
	public static void invoke(ICommand[] cmds, boolean breakCmd){
		if(breakCmd){
			for(ICommand cmd : cmds){
				cmd.execute();
			}
		}
		else{
			StringBuffer expList = new StringBuffer();
			for(ICommand cmd : cmds){
				try{
					cmd.execute();
				}
				catch(Throwable e){
					LuiLogger.error(e);
					if(e instanceof LuiRuntimeException)
						expList.append(((LuiRuntimeException)e).getHint());
					else
						expList.append(e.getMessage());
					expList.append("\r\n");
				}
			}
			if(expList.length() != 0)
				throw new LuiRuntimeException(expList.toString());
		}
	}
}
