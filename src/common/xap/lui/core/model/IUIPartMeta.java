package xap.lui.core.model;
import java.io.Serializable;
public interface IUIPartMeta extends Serializable {
	public IUIPartMeta doClone();
	public void adjustForRuntime();
	public String getEtag();
}
