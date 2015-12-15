package xap.lui.psn.top;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TabEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.parser.UIMetaParserUtil;
import xap.lui.core.services.IPaEditorService;
import xap.lui.psn.designer.CreateDesignModel;
import xap.lui.psn.pamgr.PaPalletDsListener;
public class TopFileViewController {
	public void beforeShow(DialogEvent event) {
		TextAreaComp metaComp = (TextAreaComp) getComp("file_meta");
		metaComp.setValue(PaCache.getNowViewOrPageMetaXml());
		
		String vieworedit = getState();
		if(StringUtils.equals(vieworedit, "view")){
			metaComp.setEnabled(false);
		}
	}

	private String getState() {
		String vieworedit = (String) LuiAppUtil.getAppAttr("vieworedit");
		return vieworedit;
	}
	
	public void afterItemInit_init(TabEvent event){
		UITabComp cardLayout = event.getSource();
		int currItem = cardLayout.getCurrentItem();
		String vieworedit = getState();
		if(currItem == 1){
			TextAreaComp layoutComp = (TextAreaComp) getComp("file_layout");
			layoutComp.setValue(PaCache.getNowUiMetaXml());
			if(StringUtils.equals(vieworedit, "view")){
				layoutComp.setEnabled(false);
			}
		}else if(currItem == 2){
			String filePath = PaCache.getEditorControllerPath(PaCache.getController());
			if(filePath == null)
				return;
			TextAreaComp javaComp = (TextAreaComp) getComp("file_java");
			try {
				String str = FileUtils.readFileToString(new File(filePath), "utf-8");
				javaComp.setValue(str);
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
			if(StringUtils.equals(vieworedit, "view")){
				javaComp.setEnabled(false);
			}
		}
	}
	
	private WebComp getComp(String id) {
		return LuiAppUtil.getCntViewCtx().getView().getViewComponents().getComponent(id);
	}
	// 修改源文件后保存（确定按钮）
	public void onOkClick(MouseEvent e) {
		String vieworedit = getState();
		if(StringUtils.equals(vieworedit, "view")){//若为查看文件，则返回
			return;
		}
		TextAreaComp metaComp = (TextAreaComp) getComp("file_meta");
		TextAreaComp layoutComp = (TextAreaComp) getComp("file_layout");
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		PaCache cache = PaCache.getInstance();
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String viewId = (String) PaCache.getInstance().get("_viewId");
		String resourceFolder = (String) cache.get("_resourceFolder");
		resourceFolder = resourceFolder + "/lui/nodes/";
		{
			handlerPagePartMeta(metaComp, sessionId, pageId, viewId);
		}
		{
			handlerUIPartMeta(layoutComp, sessionId, pageId, viewId);
		}
		{
			String filePath = PaCache.getEditorControllerPath(PaCache.getController());
			if(filePath != null){
				TextAreaComp javaComp = (TextAreaComp) getComp("file_java");
				handlerJavaFile(javaComp, filePath);
			}
		}
		LuiAppUtil.getCntWindowCtx().closeView("file");
		PaPalletDsListener.reloadView(pageId, viewId);
	}
	
	private void handlerJavaFile(TextAreaComp javaComp, String filePath) {
		String str = null;
		try {
			str = FileUtils.readFileToString(new File(filePath), "utf-8");
		} catch (IOException e) {
			LuiLogger.error(e.getMessage(), e);
		}
		if(StringUtils.isBlank(str)) return;
		String newValue = javaComp.getValue();
		byte bytes[] = newValue.getBytes();
		if(doChecksum(str.getBytes()) != doChecksum(bytes)){
			try {
				File file = new File(filePath);
				if (file.exists()) {
					FileUtils.write(file, newValue, "utf-8");
				}
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
	}
	private void handlerUIPartMeta(TextAreaComp layoutComp, String sessionId, String pageId, String viewId) {
//		UIPartMeta uimeta = null;
//		IPaEditorService ipaService = new xap.lui.core.services.PaEditorServiceImpl();
//		UIPartMeta uiPartMeta = ipaService.getOriUIMeta(pageId, sessionId);
//		if (StringUtils.isNotBlank(viewId)) {
//			UIViewPart uiViewPart = (UIViewPart) uiPartMeta.getElement();
//			uiPartMeta = uiViewPart.getUimeta();
//		}
//		String newValue = layoutComp.getValue().trim().replace("\r\n", "");
//		newValue = newValue.replace(" ", "");
//		newValue=newValue.replace("\n", "");
//		byte bytes[] = newValue.getBytes();
//		String oldValue=uiPartMeta.toXml().trim().replace("\r\n", "");
//		oldValue = oldValue.replace(" ", "");
//		oldValue=oldValue.replace("\n", "");
//		if (doChecksum(oldValue.getBytes()) == doChecksum(bytes)) {
//			return;
//		}
//		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
//		InputStream input = null;
//		try {
//			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
//			input = new ByteArrayInputStream(bytes);
//			
//			UIMetaParserUtil uiParserUtil = new UIMetaParserUtil();
//			if (viewId == null) {
//				uimeta = UIMetaParserUtil.parseUIMeta(pageId, null);
//			} else {
//				uimeta = new UIPartMeta();
//				UIViewPart uiViewPart = new UIViewPart();
//				uiViewPart.setId(viewId);
//				uimeta.setElement(uiViewPart);
//				parserUtil.setPagemeta(pageMeta);
//				UIPartMeta viewPartUiMeta = parserUtil.parseUIMeta(input, viewId);
//				uiViewPart.setUimeta(viewPartUiMeta);
//			}
//		} catch (Throwable e) {
//			LuiLogger.error(e.getMessage(), e);
//			throw new LuiRuntimeException(e.getMessage());
//		} finally {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e) {}
//			}
//			RequestLifeCycleContext.get().setPhase(phase);
//		}
//		ipaService.setOriUIMeta(pageId, sessionId, uimeta);
	}
	private void handlerPagePartMeta(TextAreaComp metaComp, String sessionId, String pageId, String viewId) {
		IPaEditorService ipaService = new xap.lui.core.services.PaEditorServiceImpl();
		PagePartMeta pagePartMeta = ipaService.getOriPageMeta(pageId, sessionId);
		PagePartMeta newPageMeta = null;
		ViewPartMeta viewPartMeta = null;
		InputStream input = null;
		try {
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
			if (StringUtils.isNotBlank(viewId)) {
				viewPartMeta = pagePartMeta.getWidget(viewId);
			}
			String newValue = metaComp.getValue();
			byte bytes[] = newValue.getBytes();
			if (viewPartMeta == null) {
				if (doChecksum(pagePartMeta.toXml().getBytes()) == doChecksum(bytes)) {
					return;
				}
				input = new ByteArrayInputStream(bytes);
				newPageMeta = PagePartMeta.parse(input);
			} else {
				if (doChecksum(viewPartMeta.toXml().getBytes()) == doChecksum(bytes)) {
					return;
				}
				input = new ByteArrayInputStream(bytes);
				viewPartMeta = ViewPartMeta.parse(input);
				viewPartMeta.setId(viewId);
				newPageMeta = pagePartMeta;
				newPageMeta.addWidget(viewPartMeta);
				RequestLifeCycleContext.get().setPhase(phase);
				ipaService.setOriPageMeta(pageId, sessionId, newPageMeta);
			}
			// String xml0 = newPageMeta.toXml();
			// metaComp.setValue(xml0);
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {}
			}
		}
	}
	// 校验文件内容是否改变
	private long doChecksum(byte[] bytes) {
		try {
			CRC32 crc = new CRC32();
			crc.update(bytes);
			return crc.getValue();
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	// 取消按钮
	public void onCancelClick(MouseEvent e) {
		LuiAppUtil.getCntWindowCtx().closeView("file");
	}
}
