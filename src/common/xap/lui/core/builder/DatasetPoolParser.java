package xap.lui.core.builder;

import java.io.InputStream;

import xap.lui.core.dataset.Dataset;


/**
 * update 20070904每个Dataset文件对应一个Dataset,已经没有pool的概念，以该
 * 定义文件的全路径命名该DatasetId.
 * 
 * @author lkp
 *
 */
public class DatasetPoolParser {
	
//	
	public static Dataset parse(InputStream input) {

//		Digester digester = new Digester();
//		digester.setValidating(false);
//		initRules(digester);
//
//		try {
//			return (Dataset) digester.parse(input);
//		} catch (IOException e) {
//			LuiLogger.error(e.getMessage(), e);
//			throw new LuiRuntimeException(e.getMessage());
//		} catch (SAXException e) {
//			LuiLogger.error(e.getMessage(), e);
//			throw new LuiRuntimeException(e.getMessage());
//		} finally {
//			try {
//				if (input != null)
//					input.close();
//			} catch (IOException e) {
//				LuiLogger.error(e);
//			}
//		}
		return null;
	}
//
//	private static void initRules(Digester digester) {
//		
//		String datasetClassName = Dataset.class.getName();
//		digester.addObjectCreate("Dataset", datasetClassName);
//		digester.addSetProperties("Dataset");
//
//		//initDatasetFieldRelationRule(digester);
//		initDatasetFields(digester);
//	}
//
//	private static void initDatasetFields(Digester digester) {
//		
//		String fieldsetClassName=FieldSet.class.getName();
//		digester.addObjectCreate("Dataset/Fields", fieldsetClassName);
//		digester.addSetProperties("Dataset/Fields");
//		
//		String fieldClassName=Field.class.getName();
//		digester.addObjectCreate("Dataset/Fields/Field", fieldClassName);
//		digester.addSetProperties("Dataset/Fields/Field");
//		digester.addSetNext("Dataset/Fields/Field", "addField", fieldClassName); 
//		
////		String propertyClassName = Property.class.getName();
////		digester.addObjectCreate("Dataset/Fields/Field/Properties/Property", propertyClassName);
////		digester.addSetProperties("Dataset/Fields/Field/Properties/Property");
////		
////		digester.addSetNext("Dataset/Fields/Field/Properties/Property", "addProperty", propertyClassName);
//		
////		String reffieldClassName=RefField.class.getName();
////		digester.addObjectCreate("Dataset/Fields/RefField", reffieldClassName);
////		digester.addSetProperties("Dataset/Fields/RefField");
////		digester.addSetNext("Dataset/Fields/RefField", "addField", fieldClassName);
//		
//		digester.addSetNext("Dataset/Fields", "setFieldSet", fieldsetClassName);
//		
//	}
 
//	private static void initDatasetFieldRelationRule(Digester digester) {
//		
//		String fieldrelationClassName=FieldRelation.class.getName();
//		digester.addObjectCreate("Dataset/FieldRelations/Relation", fieldrelationClassName);
//		digester.addSetProperties("Dataset/FieldRelations/Relation");
//
//		String matchFieldClassName=MatchField.class.getName();
//		digester.addObjectCreate("Dataset/FieldRelations/Relation/matchFields/Field", matchFieldClassName);
//		digester.addSetProperties("Dataset/FieldRelations/Relation/matchFields/Field");
//		digester.addSetNext("Dataset/FieldRelations/Relation/matchFields/Field", "addMatchField", matchFieldClassName);
//
//		String whereFieldsClassName=WhereField.class.getName();
//		digester.addObjectCreate("Dataset/FieldRelations/Relation/whereFields/Field", whereFieldsClassName);
//		digester.addSetProperties("Dataset/FieldRelations/Relation/whereFields/Field");
//		digester.addSetNext("Dataset/FieldRelations/Relation/whereFields/Field", "addWhereFields", whereFieldsClassName);
//
//		digester.addSetNext("Dataset/FieldRelations/Relation", "addFieldRelation", fieldrelationClassName);
//	}
}
