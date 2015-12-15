package xap.lui.core.model;

import java.io.Serializable;
import java.util.List;

public class FileValue implements Serializable{
	
	private static final long serialVersionUID = 5518714083142801609L;
	
	private String forderCode;
	private List<FileDesc> files;
	
	public String getForderCode() {
		return forderCode;
	}
	public void setForderCode(String forderCode) {
		this.forderCode = forderCode;
	}
	public List<FileDesc> getFiles() {
		return files;
	}
	public void setFiles(List<FileDesc> files) {
		this.files = files;
	}
	@Override
	public String toString() {
		return "FileValue [forderCode=" + forderCode + ", files=" + files + "]";
	}
}
