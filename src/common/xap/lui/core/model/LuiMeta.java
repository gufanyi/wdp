package xap.lui.core.model;

import xap.lui.core.layout.UIPartMeta;

public class LuiMeta {
	private LuiMeta pre = null;
	private LuiMeta next = null;
	private PagePartMeta pagePartMeta = null;
	private UIPartMeta uIPartMeta = null;
	
	
	public LuiMeta getPre() {
		return pre;
	}
	public void setPre(LuiMeta pre) {
		this.pre = pre;
	}
	public LuiMeta getNext() {
		return next;
	}
	public void setNext(LuiMeta next) {
		this.next = next;
	}
	public PagePartMeta getPagePartMeta() {
		return pagePartMeta;
	}
	public void setPagePartMeta(PagePartMeta pagePartMeta) {
		this.pagePartMeta = pagePartMeta;
	}
	public UIPartMeta getuIPartMeta() {
		return uIPartMeta;
	}
	public void setuIPartMeta(UIPartMeta uIPartMeta) {
		this.uIPartMeta = uIPartMeta;
	}
}
