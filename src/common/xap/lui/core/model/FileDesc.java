package xap.lui.core.model;

import java.io.Serializable;

public class FileDesc implements Serializable{

	private static final long serialVersionUID = 1771636301866482994L;
	
	private String name;
	private Long size;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "FileDesc [name=" + name + ", size=" + size + "]";
	}
	
}
