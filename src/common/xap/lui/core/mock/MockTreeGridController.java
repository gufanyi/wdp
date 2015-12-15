package xap.lui.core.mock;

import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.model.AppSession;

/**
 * 自定义树表型参照执行类
 * @author zhangxya
 *
 */
public class MockTreeGridController {
	public void dataLoad(DatasetEvent e){
	}
	public void okButtonClick(MouseEvent e){
	}
	
	public void afterRowSel(DatasetEvent e){
		
	}
	
	public void cancelButtonClick(MouseEvent e){
		AppSession.current().getAppContext().closeWinDialog();
	}
	
	public void textValueChanged(TextEvent e){
	}
}
