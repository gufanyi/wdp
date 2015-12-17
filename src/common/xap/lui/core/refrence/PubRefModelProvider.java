package xap.lui.core.refrence;

public interface PubRefModelProvider {

	public int getRefType(IRefModel model);

	public IRefModel getRefModel(String refCode);
}
