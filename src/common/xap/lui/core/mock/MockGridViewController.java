package xap.lui.core.mock;

import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.model.AppSession;

/**
 * 表格型自定义参照实现类controller
 * @author zhangxya
 *
 */
public abstract class MockGridViewController {
	public void dataLoad(DatasetEvent e){
	}
	public void okButtonClick(MouseEvent e){
	}
	
	public void cancelButtonClick(MouseEvent e){
		AppSession.current().getAppContext().closeWinDialog();
	}
}
