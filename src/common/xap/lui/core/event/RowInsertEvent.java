package xap.lui.core.event;

import xap.lui.core.dataset.Dataset;

public class RowInsertEvent extends DatasetEvent {
	private Integer insertedIndex;
	
	public RowInsertEvent(Dataset ds) {
		super(ds);
	}
	
	public Integer getInsertedIndex() {
		return insertedIndex;
	}
	
	public void setInsertedIndex(Integer insertedIndex) {
		this.insertedIndex = insertedIndex;
	}

}
