package xap.lui.core.xml;



public class HtmlPrintWriter extends java.io.PrintWriter {

public HtmlPrintWriter(java.io.OutputStream out) {
	super(out);
}

public HtmlPrintWriter(java.io.OutputStream out, boolean autoFlush) {
	super(out, autoFlush);
}

public HtmlPrintWriter(java.io.Writer out) {
	super(out);
}

public HtmlPrintWriter(java.io.Writer out, boolean autoFlush) {
	super(out, autoFlush);
}

public void print(String pr) {
	if (pr == null) {
		super.print(pr);
		return;
	}
	char ca[] = pr.toCharArray();
	writeExchangeChar(ca);
}

public void println() {
	super.print("<br>");
}

public void println(String str) {
	if (str == null) {
		super.write("");
		return;
	}
	char ca[] = str.toCharArray();	
	writeExchangeChar(ca);
	super.print("<br>");
}
private void writeExchangeChar(char[] ca) {
	for (int i = 0; i < ca.length; i++) {
		switch (ca[i]) {
			case '<' :
				super.print("&lt;");
				break;
			case '&' :
				super.print("&amp;");
				break;
			case '\n':
				super.print("<br>");
				break;
			case '>' :
				super.print("&gt;");
				break;
				/*
			case '\'' :
				super.print("&apos;");
				break;
			case '\"' :
				super.print("&quot;");
				break;
				*/
			case '\t':
				super.print("&nbsp;&nbsp;");
				break;
			default :
				super.write(ca[i]);
		}
	}
}
}
