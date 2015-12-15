package xap.lui.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 区域设置工具，提供了从Locale到languageId的相互转换
 * 
 * @author dengjt 2006-1-24
 */
public class LocaleUtil {
	private static final ArrayList<String> supportLang = new ArrayList<String>();
	static {
		supportLang.add("simpchn");
		supportLang.add("english");
		supportLang.add("tradchn");
	}

	private static final Map<String, Locale> langLocaleMap = new HashMap<String, Locale>();
	static {
		langLocaleMap.put("simpchn", Locale.CHINESE);
		langLocaleMap.put("english", Locale.ENGLISH);
		langLocaleMap.put("tradchn", Locale.TRADITIONAL_CHINESE);
	}

	private static final Map<Locale, String> localeLangMap = new HashMap<Locale, String>();
	static {
		localeLangMap.put(Locale.CHINESE, "simpchn");
		localeLangMap.put(Locale.ENGLISH, "english");
		localeLangMap.put(Locale.TRADITIONAL_CHINESE, "tradchn");
	}

	private static final String DEFAULT_LANG = "simpchn";


	/**
	 * @param languageId
	 * @return
	 */
	public static Locale fromLangId(String languageId) {
		Locale locale = null;
		locale = langLocaleMap.get(languageId);
		if (locale == null)
			locale = Locale.CHINESE;
		return locale;
	}

	/**
	 * @param locale
	 * @return
	 */
	public static String toLangId(Locale locale) {
		String langId = localeLangMap.get(locale);
		if (langId == null)
			langId = DEFAULT_LANG;
		return langId;
	}

	public static String getDefaultLangId() {
		return DEFAULT_LANG;
	}

}
