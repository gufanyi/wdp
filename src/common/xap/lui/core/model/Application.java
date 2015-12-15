package xap.lui.core.model;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.builder.LuiHashSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import  xap.lui.core.builder.LuiObj;
import org.apache.commons.io.IOUtils;

import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.util.JaxbMarshalFactory;
@XmlRootElement(name = "Application")
@XmlAccessorType(XmlAccessType.NONE)
public class Application extends LuiElement {
	private static final long serialVersionUID = 2528764331431629310L;
	public static final String TagName = "Application";
	@XmlAttribute
	private String defaultWindowId = null;
	@XmlAttribute
	private String type = null;
	@XmlElementRef
	private List<Connector> connectorList = new ArrayList<Connector>();
	private LuiSet<PagePartMeta> windowList = new LuiHashSet<PagePartMeta>();

	@XmlElementWrapper(name="PageMetas")
	@XmlElement(name="PageMeta")
	private LuiSet<InnerPagePart> innerPagePart=new LuiHashSet<InnerPagePart>();
	
	@XmlAttribute
	private String caption = null;
	@XmlAttribute
	private String processorClazz = null;
	@XmlAttribute
	private String controllerClazz = null;
	
	private Properties props;
	
	public void jaxbToData(){
		for(InnerPagePart inner:innerPagePart){
			PagePartMeta meta=new PagePartMeta();
			meta.setId(inner.getId());
			meta.setCaption(inner.getCaption());
			windowList.add(meta);
		}
	}
	
//	public void dataToJaxb(){
//		for(PagePartMeta pagePartMeta:windowList){
//			InnerPagePart inner = new InnerPagePart();
//			inner.setId(pagePartMeta.getId());
//			inner.setCaption(pagePartMeta.getCaption());
//			innerPagePart.add(inner);
//		}
//	}

	
	@XmlAccessorType(XmlAccessType.NONE)
	public static class InnerPagePart implements LuiObj {
		@XmlAttribute
		private String caption;
		@XmlAttribute
		private String id;
		public String getCaption() {
			return caption;
		}
		public void setCaption(String caption) {
			this.caption = caption;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	}
	
	
	public LuiSet<PagePartMeta> getWindowList() {
		return windowList;
	}
	public void setWindowList(LuiSet<PagePartMeta> windowList) {
		this.windowList = windowList;
	}
	public PagePartMeta getWindowConf(String id) {
		Iterator<PagePartMeta> it = windowList.iterator();
		while (it.hasNext()) {
			PagePartMeta win = it.next();
			if (win.getId().equals(id))
				return win;
		}
		return null;
	}
	public void addWindow(PagePartMeta window) {
		this.windowList.add(window);
		InnerPagePart inner = new InnerPagePart();
		inner.setId(window.getId());
		inner.setCaption(window.getCaption());
		this.innerPagePart.add(inner);
	}
	public void removeWin(String id){
		this.windowList.remove(this.getWin(id));
		this.innerPagePart.remove(this.getInnerWin(id));
	}
	public PagePartMeta getWin(String id) {
		if (windowList == null)
			return null;
		for (PagePartMeta us : windowList) {
			if (id.equals(us.getId())) {
				return us;
			}
		}
		return null;
	}
	public InnerPagePart getInnerWin(String id) {
		if (innerPagePart == null)
			return null;
		for (InnerPagePart us : innerPagePart) {
			if (id.equals(us.getId())) {
				return us;
			}
		}
		return null;
	}
	public String getDefaultWindowId() {
		return defaultWindowId;
	}
	public void setDefaultWindowId(String defaultWindowId) {
		this.defaultWindowId = defaultWindowId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Connector> getConnectorList() {
		return connectorList;
	}
	public void setConnectorList(List<Connector> connectorList) {
		this.connectorList = connectorList;
	}
	public void addConnector(Connector conn) {
		this.connectorList.add(conn);
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getProcessorClazz() {
		return processorClazz;
	}
	public void setProcessorClazz(String processorClazz) {
		this.processorClazz = processorClazz;
	}
	public String getControllerClazz() {
		return controllerClazz;
	}
	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}
	public Properties getProps() {
		return props;
	}
	public void setProps(Properties props) {
		this.props = props;
	}
	
	public String toXml() {
		String xmlstr= JaxbMarshalFactory.newIns().encodeXML(this);
		xmlstr=xmlstr.replace("ns2:","");
		xmlstr=xmlstr.replace("xmlns:ns2", "xmlns");
		return xmlstr;
	}
	public static Application parse(InputStream input) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Application.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Application conf = (Application) jaxbUnmarshaller.unmarshal(new StringReader(ContextResourceUtil.inputStream2String(input)));
			return conf;
		} catch (Throwable e) {
			throw new LuiRuntimeException(e);
		}finally{
			IOUtils.closeQuietly(input);
		}
	}
	public void removeConnector(Connector conn) {
		this.connectorList.remove(conn.getId());
	}
}
