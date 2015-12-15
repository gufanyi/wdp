package xap.lui.core.util;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
public class JaxbMarshalFactory {
	private static JaxbMarshalFactory factory;
	private static ThreadLocal<JaxbMarshalBox> marshalLocal = new ThreadLocal<JaxbMarshalBox>() {
		@Override
		protected JaxbMarshalBox initialValue() {
			return new JaxbMarshalBox();
		}
	};
	private JaxbMarshalFactory() {}
	public static JaxbMarshalFactory newIns() {
		if (factory == null) {
			synchronized (JaxbMarshalFactory.class) {
				if (factory == null) {
					factory = new JaxbMarshalFactory();
				}
			}
		}
		return factory;
	}
	public Marshaller lookupMarshaller(Class<?> clazz) {
		String key = clazz.getName();
		if (!marshalLocal.get().getMarshal().containsKey(key)) {
			try {
				JAXBContext jc = JAXBContext.newInstance(clazz);
				Marshaller value = jc.createMarshaller();
				value.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshalLocal.get().getMarshal().put(key, value);
			} catch (JAXBException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		return marshalLocal.get().getMarshal().get(key);
	}
	public Unmarshaller lookupUnMarshaller(Class<?> clazz) {
		String key = clazz.getName();
		if (!marshalLocal.get().getUnmarshal().containsKey(key)) {
			try {
				JAXBContext jc = JAXBContext.newInstance(clazz);
				Unmarshaller value = jc.createUnmarshaller();
				marshalLocal.get().getUnmarshal().put(key, value);
			} catch (JAXBException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}
		return marshalLocal.get().getUnmarshal().get(key);
	}
	public String encodeXML(Object jaxbElement) {
		StringWriter writer = new StringWriter();
		if (jaxbElement == null)
			return StringUtils.EMPTY;
		Class<?> clazz = jaxbElement.getClass();
		try {
			Marshaller m = this.lookupMarshaller(clazz);
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			if (jaxbElement instanceof ViewPartMeta || jaxbElement instanceof PagePartMeta) {
				m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://com.founer.xap/schema/lui http://com.founer.xap/schema/lui/view.xsd");
			}
			m.marshal(jaxbElement, writer);
			return writer.toString();
		} catch (Throwable e) {
			e.printStackTrace();
			return StringUtils.EMPTY;
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	@SuppressWarnings("unchecked")
	public <T> T decodeXML(Class<T> clazz, String xml) {
		if (xml == null)
			return null;
		Reader reader = new StringReader(xml);
		try {
			return (T) this.lookupUnMarshaller(clazz).unmarshal(reader);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}
}
class JaxbMarshalBox {
	private Map<String, Marshaller> marshal = Collections.synchronizedMap(new HashMap<String, Marshaller>());
	private Map<String, Unmarshaller> unmarshal = Collections.synchronizedMap(new HashMap<String, Unmarshaller>());
	public Map<String, Marshaller> getMarshal() {
		return marshal;
	}
	public Map<String, Unmarshaller> getUnmarshal() {
		return unmarshal;
	}
}
