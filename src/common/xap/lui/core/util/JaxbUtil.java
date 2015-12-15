package xap.lui.core.util;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;


public class JaxbUtil {
	public static String toXml(Object object) {
//		class MyNamespacePrefixMapper extends NamespacePrefixMapper {
//			
//			
//			@Override
//			public String[] getContextualNamespaceDecls() {
//				// TODO Auto-generated method stub
//				return super.getContextualNamespaceDecls();
//			}
//
//			@Override
//			public String[] getPreDeclaredNamespaceUris() {
//				// TODO Auto-generated method stub
//				return super.getPreDeclaredNamespaceUris();
//			}
//
//			@Override
//			public String[] getPreDeclaredNamespaceUris2() {
//				// TODO Auto-generated method stub
//				return super.getPreDeclaredNamespaceUris2();
//			}
//
//			public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
//				if (namespaceUri == null || namespaceUri.length() == 0) {
//					return "";
//				}
//				if ("http://com.founer.xap/schema/lui".equalsIgnoreCase(namespaceUri)) {
//					requirePrefix = false;
//				} else {
//					requirePrefix = true;
//				}
//				if (requirePrefix) {
//					return "xap";
//				} else {
//					return "";
//				}
//			}
//		}
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = jc.createMarshaller();
			// 设置编码
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			// 设置是否要格式化输出
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// 注册生成命名空间的内部类
			//marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper());
			// 创建输出流
			Writer outPut = new StringWriter();
			// 开始序列化
			marshaller.marshal(object, outPut);
			return outPut.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object parser(Class<?> clazz, Reader reader) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(clazz);
			Unmarshaller us = jc.createUnmarshaller();
			// 获取xmlinput工厂
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// 创建XMLStreamReader
			XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
			// 构造Reader的委托类
			StreamReaderDelegate delegatingStreamReader = new StreamReaderDelegate(streamReader) {
				String namespaceURI = "";

				public String getNamespaceURI() {
					namespaceURI = super.getNamespaceURI();
					return super.getNamespaceURI();
				}

				public String getPrefix() {
					if ("http://com.founer.xap/schema/lui".equalsIgnoreCase(namespaceURI)) {
						return "";
					}
					return super.getPrefix();
				}
			};
			// 开始解析
			Object model = us.unmarshal(delegatingStreamReader);
			return model;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
