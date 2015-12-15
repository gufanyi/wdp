package xap.lui.core.model;

import xap.lui.core.constant.ExtAttrConstants;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.MdDataset;


public final class DsRelationAdjuster {
	private ViewPartMeta widget;
	public DsRelationAdjuster(ViewPartMeta widget){
		this.widget = widget;
	}
	
	public void adjust() {
		DatasetRelations dsRelations = widget.getViewModels().getDsrelations();
		if(dsRelations != null){
			DatasetRelation[] rels = dsRelations.getDsRelations();
			if(rels != null){
				for (int i = 0; i < rels.length; i++) {
					DatasetRelation rel = rels[i];
					String masterDsId = rel.getMasterDataset();
					String slaveDsId = rel.getDetailDataset();
					Dataset masterDs = widget.getViewModels().getDataset(masterDsId);
					Dataset slaveDs = widget.getViewModels().getDataset(slaveDsId);
					if(slaveDs == null){
						// LuiLogger.error(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
						// "DsRelationAdjuster-000000")/*设置了不正确的rel id:*/ +
						// rel.getId());
						return;
					}
					if((slaveDs.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD)) == null && (masterDs instanceof MdDataset) && (slaveDs instanceof MdDataset)){
//						try{
//							IBusinessEntity entity = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(((MdDataset)masterDs).getObjMeta());
//							IBusinessEntity childEntity = MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(((MdDataset)slaveDs).getObjMeta());
//							List<IAssociation> list = entity.getAssociations();
//							if(list != null){
//								Iterator<IAssociation> assIt = list.iterator();
//								while(assIt.hasNext()){
//									IAssociation ass = assIt.next();
//									if(ass.getEndElement().getAssElement().getID().equals(childEntity.getID())){
//											String parentField = ass.getStartAttribute().getName();
//											slaveDs.setExtendAttribute(ExtAttrConstants.PARENT_FIELD, parentField);
//											break;
//										}
//								}
//							}
//						}
//						catch(Exception e){
//							LuiLogger.error(e.getMessage(), e);
//						}
					}
				}
			}
		}
	}
}
