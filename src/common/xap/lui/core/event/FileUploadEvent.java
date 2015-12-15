package xap.lui.core.event;
import xap.lui.core.comps.FileUploadComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
/**
 * @author guoweic
 *
 */
public class FileUploadEvent extends AbstractServerEvent<FileUploadComp> {
	public static final String ON_UPLOAD = "onUpload";
	public FileUploadEvent(FileUploadComp webElement) {
		super(webElement);
	}
	public FileUploadEvent() {}
	public static LuiEventConf getOnUploadEvent() {
		LuiEventConf event = new LuiEventConf();
		event.setEventName(ON_UPLOAD);
		LuiParameter param = new LuiParameter();
		event.addParam(param);
		EventSubmitRule esb = new EventSubmitRule();
		event.setSubmitRule(esb);
		return event;
	}
	@Override
	public String getJsClazz() {
		return "FileListener";
	}
}
