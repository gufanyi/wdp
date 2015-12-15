package xap.lui.core.jstools;

import java.util.List;

public class JsSyntaxVerifier implements ISyntaxVerifier {

	public VerifyInfo[] verify(String[] lines) {
		return null;
	}

	public VerifyInfo[] verify(List<String> lines) {
		if(lines != null)
			return verify(lines.toArray(new String[0]));
		return null;
	}
	
}
