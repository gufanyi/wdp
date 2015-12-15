package xap.lui.core.render;

import java.util.Map;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.StringInputItem;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartMeta;


/**
 * FormComp动态添加字段命令
 * 
 * @author liujmc
 * @since uapweb6.12
 */
public class AddFormElementCmd extends AbstractRaCommand {

	public AddFormElementCmd(RaParameter rp) {
		super(rp);
	}

	@Override
	public void execute() {
		ViewPartMeta widget = rp.getPageMeta().getWidget(rp.getWidgetId());
		FormComp formComp = (FormComp)widget.getViewComponents().getComponent(rp.getEleId());
		UIElement uiForm = UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId());
		
		InputItem idInputItem = new StringInputItem("id", "编号", true);
		InputItem titleInputItem = new StringInputItem("title","名称", true);
		InteractionUtil.showInputDialog("确认", new InputItem[] { idInputItem, titleInputItem});
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		if (rs != null) {
			
			String id = rs.get("id");
			if(formComp.getElementById(id) != null){
				throw new LuiRuntimeException("该项已经存在!");
			}
			String title = rs.get("title");
			Dataset ds = widget.getViewModels().getDataset(formComp.getDataset());
			Field field = new Field(id);
			field.setText(title);

			ds.addField(field);
			
			FormElement formEle = new FormElement();
			formEle.setId(id);
			formEle.setField(id);
			formEle.setEditorType(EditorTypeConst.STRINGTEXT);
			formEle.setText(title);
			formEle.setVisible(true);
			formComp.addElement(formEle);
			Class<? extends Object> c = formComp.getClass();
			ILuiRender render =uiForm.getRender();
			String divId = render.getDivId();
			AppSession.current().getAppContext().addExecScript("execDynamicScript2RemoveComponent('"+divId+"','"+widget.getId()+"','"+uiForm.getId()+"');\n");
			CmdInvoker.invoke(new RepaintCmd(rp));
		}
	}
}
