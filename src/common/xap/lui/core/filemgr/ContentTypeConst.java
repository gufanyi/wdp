package xap.lui.core.filemgr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * ContentType类型
 * @author licza
 *
 */
public class ContentTypeConst {
	private static Map<String,String> ContentTypeMap = new HashMap<String,String>();
	private static Map<String,String> ViewTypeMap = new HashMap<String,String>();
	private static Set<String> OfficeType = new HashSet<String>();
	
	static {
			ContentTypeMap.put("nan" , "text/html");
			ContentTypeMap.put("zip" , "application/zip");
			ContentTypeMap.put("jar" , "application/zip");
			ContentTypeMap.put("rar" , "application/zip");
			ContentTypeMap.put("7z" , "application/zip");
			ContentTypeMap.put("001" , "application/x-001");
			ContentTypeMap.put("301" , "application/x-301");
			ContentTypeMap.put("323" , "text/h323");
			ContentTypeMap.put("906" , "application/x-906");
			ContentTypeMap.put("907" , "drawing/907");
			ContentTypeMap.put("a11" , "application/x-a11");
			ContentTypeMap.put("acp" , "audio/x-mei-aac");
			ContentTypeMap.put("ai" , "application/postscript");
			ContentTypeMap.put("aif" , "audio/aiff");
			ContentTypeMap.put("aifc" , "audio/aiff");
			ContentTypeMap.put("aiff" , "audio/aiff");
			ContentTypeMap.put("anv" , "application/x-anv");
			ContentTypeMap.put("asa" , "text/asa");
			ContentTypeMap.put("asf" , "video/x-ms-asf");
			ContentTypeMap.put("asp" , "text/asp");
			ContentTypeMap.put("asx" , "video/x-ms-asf");
			ContentTypeMap.put("au" , "audio/basic");
			ContentTypeMap.put("avi" , "video/avi");
			ContentTypeMap.put("awf" , "application/vnd.adobe.workflow");
			ContentTypeMap.put("biz" , "text/xml");
			ContentTypeMap.put("bmp" , "application/x-bmp");
			ContentTypeMap.put("bot" , "application/x-bot");
			ContentTypeMap.put("c4t" , "application/x-c4t");
			ContentTypeMap.put("c90" , "application/x-c90");
			ContentTypeMap.put("cal" , "application/x-cals");
			ContentTypeMap.put("cat" , "application/vnd.ms-pki.seccat");
			ContentTypeMap.put("cdf" , "application/x-netcdf");
			ContentTypeMap.put("cdr" , "application/x-cdr");
			ContentTypeMap.put("cel" , "application/x-cel");
			ContentTypeMap.put("cer" , "application/x-x509-ca-cert");
			ContentTypeMap.put("cg4" , "application/x-g4");
			ContentTypeMap.put("cgm" , "application/x-cgm");
			ContentTypeMap.put("cit" , "application/x-cit");
			ContentTypeMap.put("class" , "java/*");
			ContentTypeMap.put("cml" , "text/xml");
			ContentTypeMap.put("cmp" , "application/x-cmp");
			ContentTypeMap.put("cmx" , "application/x-cmx");
			ContentTypeMap.put("cot" , "application/x-cot");
			ContentTypeMap.put("crl" , "application/pkix-crl");
			ContentTypeMap.put("crt" , "application/x-x509-ca-cert");
			ContentTypeMap.put("csi" , "application/x-csi");
			ContentTypeMap.put("css" , "text/css");
			ContentTypeMap.put("cut" , "application/x-cut");
			ContentTypeMap.put("dbf" , "application/x-dbf");
			ContentTypeMap.put("dbm" , "application/x-dbm");
			ContentTypeMap.put("dbx" , "application/x-dbx");
			ContentTypeMap.put("dcd" , "text/xml");
			ContentTypeMap.put("dcx" , "application/x-dcx");
			ContentTypeMap.put("der" , "application/x-x509-ca-cert");
			ContentTypeMap.put("dgn" , "application/x-dgn");
			ContentTypeMap.put("dib" , "application/x-dib");
			ContentTypeMap.put("dll" , "application/x-msdownload");
			ContentTypeMap.put("doc" , "application/msword");
			ContentTypeMap.put("dot" , "application/msword");
			ContentTypeMap.put("drw" , "application/x-drw");
			ContentTypeMap.put("dtd" , "text/xml");
			ContentTypeMap.put("dwf" , "Model/vnd.dwf");
			ContentTypeMap.put("dwf" , "application/x-dwf");
			ContentTypeMap.put("dwg" , "application/x-dwg");
			ContentTypeMap.put("dxb" , "application/x-dxb");
			ContentTypeMap.put("dxf" , "application/x-dxf");
			ContentTypeMap.put("edn" , "application/vnd.adobe.edn");
			ContentTypeMap.put("emf" , "application/x-emf");
			ContentTypeMap.put("eml" , "message/rfc822");
			ContentTypeMap.put("ent" , "text/xml");
			ContentTypeMap.put("epi" , "application/x-epi");
			ContentTypeMap.put("eps" , "application/x-ps");
			ContentTypeMap.put("eps" , "application/postscript");
			ContentTypeMap.put("etd" , "application/x-ebx");
			ContentTypeMap.put("exe" , "application/x-msdownload");
			ContentTypeMap.put("fax" , "image/fax");
			ContentTypeMap.put("fdf" , "application/vnd.fdf");
			ContentTypeMap.put("fif" , "application/fractals");
			ContentTypeMap.put("fo" , "text/xml");
			ContentTypeMap.put("frm" , "application/x-frm");
			ContentTypeMap.put("g4" , "application/x-g4");
			ContentTypeMap.put("gbr" , "application/x-gbr");
			ContentTypeMap.put("gcd" , "application/x-gcd");
			ContentTypeMap.put("gif" , "image/gif");
			ContentTypeMap.put("gl2" , "application/x-gl2");
			ContentTypeMap.put("gp4" , "application/x-gp4");
			ContentTypeMap.put("hgl" , "application/x-hgl");
			ContentTypeMap.put("hmr" , "application/x-hmr");
			ContentTypeMap.put("hpg" , "application/x-hpgl");
			ContentTypeMap.put("hpl" , "application/x-hpl");
			ContentTypeMap.put("hqx" , "application/mac-binhex40");
			ContentTypeMap.put("hrf" , "application/x-hrf");
			ContentTypeMap.put("hta" , "application/hta");
			ContentTypeMap.put("htc" , "text/x-component");
			ContentTypeMap.put("htm" , "text/html");
			ContentTypeMap.put("html" , "text/html");
			ContentTypeMap.put("htt" , "text/webviewhtml");
			ContentTypeMap.put("htx" , "text/html");
			ContentTypeMap.put("icb" , "application/x-icb");
			ContentTypeMap.put("ico" , "image/x-icon");
			ContentTypeMap.put("ico" , "application/x-ico");
			ContentTypeMap.put("iff" , "application/x-iff");
			ContentTypeMap.put("ig4" , "application/x-g4");
			ContentTypeMap.put("igs" , "application/x-igs");
			ContentTypeMap.put("iii" , "application/x-iphone");
			ContentTypeMap.put("img" , "application/x-img");
			ContentTypeMap.put("ins" , "application/x-internet-signup");
			ContentTypeMap.put("isp" , "application/x-internet-signup");
			ContentTypeMap.put("IVF" , "video/x-ivf");
			ContentTypeMap.put("java" , "java/*");
			ContentTypeMap.put("jfif" , "image/jpeg");
			ContentTypeMap.put("jpe" , "image/jpeg");
			ContentTypeMap.put("jpe" , "application/x-jpe");
			ContentTypeMap.put("jpeg" , "image/jpeg");
			ContentTypeMap.put("jpg" , "image/jpeg");
			ContentTypeMap.put("js" , "application/x-javascript");
			ContentTypeMap.put("jsp" , "text/html");
			ContentTypeMap.put("la1" , "audio/x-liquid-file");
			ContentTypeMap.put("lar" , "application/x-laplayer-reg");
			ContentTypeMap.put("latex" , "application/x-latex");
			ContentTypeMap.put("lavs" , "audio/x-liquid-secure");
			ContentTypeMap.put("lbm" , "application/x-lbm");
			ContentTypeMap.put("lmsff" , "audio/x-la-lms");
			ContentTypeMap.put("ls" , "application/x-javascript");
			ContentTypeMap.put("ltr" , "application/x-ltr");
			ContentTypeMap.put("m1v" , "video/x-mpeg");
			ContentTypeMap.put("m2v" , "video/x-mpeg");
			ContentTypeMap.put("m3u" , "audio/mpegurl");
			ContentTypeMap.put("m4e" , "video/mpeg4");
			ContentTypeMap.put("mac" , "application/x-mac");
			ContentTypeMap.put("man" , "application/x-troff-man");
			ContentTypeMap.put("math" , "text/xml");
			ContentTypeMap.put("mdb" , "application/msaccess");
			ContentTypeMap.put("mdb" , "application/x-mdb");
			ContentTypeMap.put("mfp" , "application/x-shockwave-flash");
			ContentTypeMap.put("mht" , "message/rfc822");
			ContentTypeMap.put("mhtml" , "message/rfc822");
			ContentTypeMap.put("mi" , "application/x-mi");
			ContentTypeMap.put("mid" , "audio/mid");
			ContentTypeMap.put("midi" , "audio/mid");
			ContentTypeMap.put("mil" , "application/x-mil");
			ContentTypeMap.put("mml" , "text/xml");
			ContentTypeMap.put("mnd" , "audio/x-musicnet-download");
			ContentTypeMap.put("mns" , "audio/x-musicnet-stream");
			ContentTypeMap.put("mocha" , "application/x-javascript");
			ContentTypeMap.put("movie" , "video/x-sgi-movie");
			ContentTypeMap.put("mp1" , "audio/mp1");
			ContentTypeMap.put("mp2" , "audio/mp2");
			ContentTypeMap.put("mp2v" , "video/mpeg");
			ContentTypeMap.put("mp3" , "audio/mp3");
			ContentTypeMap.put("mp4" , "");
			ContentTypeMap.put("flv" , "");
			ContentTypeMap.put("mpa" , "video/x-mpg");
			ContentTypeMap.put("mpd" , "application/vnd.ms-project");
			ContentTypeMap.put("mpe" , "video/x-mpeg");
			ContentTypeMap.put("mpeg" , "video/mpg");
			ContentTypeMap.put("mpg" , "video/mpg");
			ContentTypeMap.put("mpga" , "audio/rn-mpeg");
			ContentTypeMap.put("mpp" , "application/vnd.ms-project");
			ContentTypeMap.put("mps" , "video/x-mpeg");
			ContentTypeMap.put("mpt" , "application/vnd.ms-project");
			ContentTypeMap.put("mpv" , "video/mpg");
			ContentTypeMap.put("mpv2" , "video/mpeg");
			ContentTypeMap.put("mpw" , "application/vnd.ms-project");
			ContentTypeMap.put("mpx" , "application/vnd.ms-project");
			ContentTypeMap.put("mtx" , "text/xml");
			ContentTypeMap.put("mxp" , "application/x-mmxp");
			ContentTypeMap.put("net" , "image/pnetvue");
			ContentTypeMap.put("nrf" , "application/x-nrf");
			ContentTypeMap.put("nws" , "message/rfc822");
			ContentTypeMap.put("odc" , "text/x-ms-odc");
			ContentTypeMap.put("out" , "application/x-out");
			ContentTypeMap.put("p10" , "application/pkcs10");
			ContentTypeMap.put("p12" , "application/x-pkcs12");
			ContentTypeMap.put("p7b" , "application/x-pkcs7-certificates");
			ContentTypeMap.put("p7c" , "application/pkcs7-mime");
			ContentTypeMap.put("p7m" , "application/pkcs7-mime");
			ContentTypeMap.put("p7r" , "application/x-pkcs7-certreqresp");
			ContentTypeMap.put("p7s" , "application/pkcs7-signature");
			ContentTypeMap.put("pc5" , "application/x-pc5");
			ContentTypeMap.put("pci" , "application/x-pci");
			ContentTypeMap.put("pcl" , "application/x-pcl");
			ContentTypeMap.put("pcx" , "application/x-pcx");
			ContentTypeMap.put("pdf" , "application/pdf");
			ContentTypeMap.put("pdx" , "application/vnd.adobe.pdx");
			ContentTypeMap.put("pfx" , "application/x-pkcs12");
			ContentTypeMap.put("pgl" , "application/x-pgl");
			ContentTypeMap.put("pic" , "application/x-pic");
			ContentTypeMap.put("pko" , "application/vnd.ms-pki.pko");
			ContentTypeMap.put("pl" , "application/x-perl");
			ContentTypeMap.put("plg" , "text/html");
			ContentTypeMap.put("pls" , "audio/scpls");
			ContentTypeMap.put("plt" , "application/x-plt");
			ContentTypeMap.put("png" , "image/png");
			ContentTypeMap.put("pot" , "application/vnd.ms-powerpoint");
			ContentTypeMap.put("ppa" , "application/vnd.ms-powerpoint");
			ContentTypeMap.put("ppm" , "application/x-ppm");
			ContentTypeMap.put("pps" , "application/vnd.ms-powerpoint");
			ContentTypeMap.put("ppt" , "application/vnd.ms-powerpoint");
			ContentTypeMap.put("pr" , "application/x-pr");
			ContentTypeMap.put("prf" , "application/pics-rules");
			ContentTypeMap.put("prn" , "application/x-prn");
			ContentTypeMap.put("prt" , "application/x-prt");
			ContentTypeMap.put("ps" , "application/x-ps");
			ContentTypeMap.put("ps" , "application/postscript");
			ContentTypeMap.put("ptn" , "application/x-ptn");
			ContentTypeMap.put("pwz" , "application/vnd.ms-powerpoint");
			ContentTypeMap.put("r3t" , "text/vnd.rn-realtext3d");
			ContentTypeMap.put("ra" , "audio/vnd.rn-realaudio");
			ContentTypeMap.put("ram" , "audio/x-pn-realaudio");
			ContentTypeMap.put("ras" , "application/x-ras");
			ContentTypeMap.put("rat" , "application/rat-file");
			ContentTypeMap.put("rdf" , "text/xml");
			ContentTypeMap.put("rec" , "application/vnd.rn-recording");
			ContentTypeMap.put("red" , "application/x-red");
			ContentTypeMap.put("rgb" , "application/x-rgb");
			ContentTypeMap.put("rjs" , "application/vnd.rn-realsystem-rjs");
			ContentTypeMap.put("rjt" , "application/vnd.rn-realsystem-rjt");
			ContentTypeMap.put("rlc" , "application/x-rlc");
			ContentTypeMap.put("rle" , "application/x-rle");
			ContentTypeMap.put("rm" , "application/vnd.rn-realmedia");
			ContentTypeMap.put("rmf" , "application/vnd.adobe.rmf");
			ContentTypeMap.put("rmi" , "audio/mid");
			ContentTypeMap.put("rmj" , "application/vnd.rn-realsystem-rmj");
			ContentTypeMap.put("rmm" , "audio/x-pn-realaudio");
			ContentTypeMap.put("rmp" , "application/vnd.rn-rn_music_package");
			ContentTypeMap.put("rms" , "application/vnd.rn-realmedia-secure");
			ContentTypeMap.put("rmvb" , "application/vnd.rn-realmedia-vbr");
			ContentTypeMap.put("rmx" , "application/vnd.rn-realsystem-rmx");
			ContentTypeMap.put("rnx" , "application/vnd.rn-realplayer");
			ContentTypeMap.put("rp" , "image/vnd.rn-realpix");
			ContentTypeMap.put("rpm" , "audio/x-pn-realaudio-plugin");
			ContentTypeMap.put("rsml" , "application/vnd.rn-rsml");
			ContentTypeMap.put("rt" , "text/vnd.rn-realtext");
			ContentTypeMap.put("rtf" , "application/msword");
			ContentTypeMap.put("rtf" , "application/x-rtf");
			ContentTypeMap.put("rv" , "video/vnd.rn-realvideo");
			ContentTypeMap.put("sam" , "application/x-sam");
			ContentTypeMap.put("sat" , "application/x-sat");
			ContentTypeMap.put("sdp" , "application/sdp");
			ContentTypeMap.put("sdw" , "application/x-sdw");
			ContentTypeMap.put("sit" , "application/x-stuffit");
			ContentTypeMap.put("slb" , "application/x-slb");
			ContentTypeMap.put("sld" , "application/x-sld");
			ContentTypeMap.put("slk" , "drawing/x-slk");
			ContentTypeMap.put("smi" , "application/smil");
			ContentTypeMap.put("smil" , "application/smil");
			ContentTypeMap.put("smk" , "application/x-smk");
			ContentTypeMap.put("snd" , "audio/basic");
			ContentTypeMap.put("sol" , "text/plain");
			ContentTypeMap.put("sor" , "text/plain");
			ContentTypeMap.put("spc" , "application/x-pkcs7-certificates");
			ContentTypeMap.put("spl" , "application/futuresplash");
			ContentTypeMap.put("spp" , "text/xml");
			ContentTypeMap.put("ssm" , "application/streamingmedia");
			ContentTypeMap.put("sst" , "application/vnd.ms-pki.certstore");
			ContentTypeMap.put("stl" , "application/vnd.ms-pki.stl");
			ContentTypeMap.put("stm" , "text/html");
			ContentTypeMap.put("sty" , "application/x-sty");
			ContentTypeMap.put("svg" , "text/xml");
			ContentTypeMap.put("swf" , "application/x-shockwave-flash");
			ContentTypeMap.put("tdf" , "application/x-tdf");
			ContentTypeMap.put("tg4" , "application/x-tg4");
			ContentTypeMap.put("tga" , "application/x-tga");
			ContentTypeMap.put("tif" , "image/tiff");
			ContentTypeMap.put("tif" , "application/x-tif");
			ContentTypeMap.put("tiff" , "image/tiff");
			ContentTypeMap.put("tld" , "text/xml");
			ContentTypeMap.put("top" , "drawing/x-top");
			ContentTypeMap.put("torrent" , "application/x-bittorrent");
			ContentTypeMap.put("tsd" , "text/xml");
			ContentTypeMap.put("txt" , "text/plain");
			ContentTypeMap.put("sql" , "text/plain");
			ContentTypeMap.put("uin" , "application/x-icq");
			ContentTypeMap.put("uls" , "text/iuls");
			ContentTypeMap.put("vcf" , "text/x-vcard");
			ContentTypeMap.put("vda" , "application/x-vda");
			ContentTypeMap.put("vdx" , "application/vnd.visio");
			ContentTypeMap.put("vml" , "text/xml");
			ContentTypeMap.put("vpg" , "application/x-vpeg005");
			ContentTypeMap.put("vsd" , "application/vnd.visio");
			ContentTypeMap.put("vsd" , "application/x-vsd");
			ContentTypeMap.put("vss" , "application/vnd.visio");
			ContentTypeMap.put("vst" , "application/vnd.visio");
			ContentTypeMap.put("vst" , "application/x-vst");
			ContentTypeMap.put("vsw" , "application/vnd.visio");
			ContentTypeMap.put("vsx" , "application/vnd.visio");
			ContentTypeMap.put("vtx" , "application/vnd.visio");
			ContentTypeMap.put("vxml" , "text/xml");
			ContentTypeMap.put("wav" , "audio/wav");
			ContentTypeMap.put("wax" , "audio/x-ms-wax");
			ContentTypeMap.put("wb1" , "application/x-wb1");
			ContentTypeMap.put("wb2" , "application/x-wb2");
			ContentTypeMap.put("wb3" , "application/x-wb3");
			ContentTypeMap.put("wbmp" , "image/vnd.wap.wbmp");
			ContentTypeMap.put("wiz" , "application/msword");
			ContentTypeMap.put("wk3" , "application/x-wk3");
			ContentTypeMap.put("wk4" , "application/x-wk4");
			ContentTypeMap.put("wkq" , "application/x-wkq");
			ContentTypeMap.put("wks" , "application/x-wks");
			ContentTypeMap.put("wm" , "video/x-ms-wm");
			ContentTypeMap.put("wma" , "audio/x-ms-wma");
			ContentTypeMap.put("wmd" , "application/x-ms-wmd");
			ContentTypeMap.put("wmf" , "application/x-wmf");
			ContentTypeMap.put("wml" , "text/vnd.wap.wml");
			ContentTypeMap.put("wmv" , "video/x-ms-wmv");
			ContentTypeMap.put("wmx" , "video/x-ms-wmx");
			ContentTypeMap.put("wmz" , "application/x-ms-wmz");
			ContentTypeMap.put("wp6" , "application/x-wp6");
			ContentTypeMap.put("wpd" , "application/x-wpd");
			ContentTypeMap.put("wpg" , "application/x-wpg");
			ContentTypeMap.put("wpl" , "application/vnd.ms-wpl");
			ContentTypeMap.put("wq1" , "application/x-wq1");
			ContentTypeMap.put("wr1" , "application/x-wr1");
			ContentTypeMap.put("wri" , "application/x-wri");
			ContentTypeMap.put("wrk" , "application/x-wrk");
			ContentTypeMap.put("ws" , "application/x-ws");
			ContentTypeMap.put("ws2" , "application/x-ws");
			ContentTypeMap.put("wsc" , "text/scriptlet");
			ContentTypeMap.put("wsdl" , "text/xml");
			ContentTypeMap.put("wvx" , "video/x-ms-wvx");
			ContentTypeMap.put("xdp" , "application/vnd.adobe.xdp");
			ContentTypeMap.put("xdr" , "text/xml");
			ContentTypeMap.put("xfd" , "application/vnd.adobe.xfd");
			ContentTypeMap.put("xfdf" , "application/vnd.adobe.xfdf");
			ContentTypeMap.put("xhtml" , "text/html");
			ContentTypeMap.put("xls" , "application/vnd.ms-excel");
			ContentTypeMap.put("xlw" , "application/x-xlw");
			ContentTypeMap.put("xml" , "text/xml");
			ContentTypeMap.put("xpl" , "audio/scpls");
			ContentTypeMap.put("xq" , "text/xml");
			ContentTypeMap.put("xql" , "text/xml");
			ContentTypeMap.put("xquery" , "text/xml");
			ContentTypeMap.put("xsd" , "text/xml");
			ContentTypeMap.put("xsl" , "text/xml");
			ContentTypeMap.put("xslt" , "text/xml");
			ContentTypeMap.put("xwd" , "application/x-xwd");
			ContentTypeMap.put("x_b" , "application/x-x_b");
			ContentTypeMap.put("x_t" , "application/x-x_t");
			ContentTypeMap.put("ceb" , "application/ceb");
			ContentTypeMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			ContentTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			ContentTypeMap.put("fbr" , "application/zip");
			ContentTypeMap.put("c" , "application/zip");
			ContentTypeMap.put("h" , "application/zip");
			ContentTypeMap.put("log" , "application/zip");
			ContentTypeMap.put("ini" , "application/zip");
			ContentTypeMap.put("inf" , "application/zip");
			ContentTypeMap.put("bat" , "application/zip");
			ContentTypeMap.put("db" , "application/zip");
			ContentTypeMap.put("dat" , "application/zip");
			ContentTypeMap.put("cs" , "application/zip");
			ContentTypeMap.put("url" , "application/zip");
			ContentTypeMap.put("php" , "application/zip");
			ContentTypeMap.put("aspx" , "application/zip");
			ContentTypeMap.put("chm" , "application/zip");
			ContentTypeMap.put("reg" , "application/zip");

			
			OfficeType.add("doc");
			OfficeType.add("docx");
			OfficeType.add("rtf");
			OfficeType.add("xlsx");
			OfficeType.add("xls");
			OfficeType.add("ppt");
			OfficeType.add("pptx");
			
			ViewTypeMap.put("text/html", "txt");
			ViewTypeMap.put("application/x-img", "img");
			ViewTypeMap.put("application/msword", "office");
			ViewTypeMap.put("application/vnd.ms-excel", "office");
			ViewTypeMap.put("application/vnd.ms-powerpoint", "office");
			ViewTypeMap.put("application/vnd.ms-powerpoint", "office");
			
			ViewTypeMap.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "office");
			ViewTypeMap.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "office");
			
			ViewTypeMap.put("text/xml", "txt");
			
	}
	/**
	 * 获得内容类型
	 * @param type
	 * @return
	 */
	private static String getContentType(String type){
		if(StringUtils.isEmpty(type))
			return "application/zip";
		String key = type.toLowerCase(); 
		return ContentTypeMap.get(key);
	}
	
	private static boolean isoffice(String type){
		return OfficeType.contains(type.toLowerCase());
		
	}
	private static String getViewType(String contentType){
		if(ViewTypeMap.containsKey(contentType))
			return ViewTypeMap.get(contentType);
		return "";
	} 
}
