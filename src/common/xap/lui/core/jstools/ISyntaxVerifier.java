package xap.lui.core.jstools;

import java.util.List;


public interface ISyntaxVerifier {
	public VerifyInfo[] verify(String[] lines);
	public VerifyInfo[] verify(List<String> lines);
}
