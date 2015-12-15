package xap.lui.core.mock;

import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.KeyEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.model.AppSession;

/**
 * 模拟Tree界面Controller基类
 * @author dengjt
 *
 */
public abstract class MockTreeViewController {
	public void dataLoad(DatasetEvent e){
	}
	public void okButtonClick(MouseEvent e){
	}
	
	public void cancelButtonClick(MouseEvent e){
		AppSession.current().getAppContext().closeWinDialog();
	}
	public void onTreeDbClick(TreeNodeEvent e){
		
	}
	public void onLocate(KeyEvent e){
		
	}
}
