package xap.lui.core.reduce;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.jstools.CompressorUtil;
import xap.lui.core.logger.LuiLogger;


/**
 * JS代码压缩服务类
 * 
 * @author gd 2010-4-23
 * 
 */
public class JsCompressServiceImpl implements IJsCompressionService {

	@Override
	public String compressCss(String[] cssFiles) {
		InputStream[] inputs = null;
		try{
			inputs = new FileInputStream[cssFiles.length];
			for (int i = 0; i < cssFiles.length; i++) {
				String filePath = cssFiles[i];
				inputs[i] = new FileInputStream(filePath);
			}
			return CompressorUtil.compressCss(inputs);
		}
		catch(Exception e){
			LuiLogger.error(e);
		}
		finally{
			if(inputs != null){
				for (int i = 0; i < inputs.length; i++) {
					try {
						inputs[i].close();
					} 
					catch (IOException e) {
						LuiLogger.error(e);
					}
				}
			}
		}
		return null;
	}

	@Override
	public String compressJs(String[] jsFiles) {
		InputStream[] inputs = null;
		try{
			inputs = new FileInputStream[jsFiles.length];
			for (int i = 0; i < jsFiles.length; i++) {
				String filePath = jsFiles[i];
				File file = ContextResourceUtil.getLuiFile(filePath);
				inputs[i] = new FileInputStream(file);
			}
			return CompressorUtil.compressJs(inputs);
		}
		catch(Exception e){
			LuiLogger.error(e);
		}
		finally{
			if(inputs != null){
				for (int i = 0; i < inputs.length; i++) {
					try {
						inputs[i].close();
					} 
					catch (IOException e) {
						LuiLogger.error(e);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public String noCompressCss(String[] cssFiles) {
		InputStream[] inputs = null;
		try{
			inputs = new FileInputStream[cssFiles.length];
			for (int i = 0; i < cssFiles.length; i++) {
				String filePath = cssFiles[i];
				inputs[i] = new FileInputStream(filePath);
			}
			return CompressorUtil.noCompressCss(inputs);
		}
		catch(Exception e){
			LuiLogger.error(e);
		}
		finally{
			if(inputs != null){
				for (int i = 0; i < inputs.length; i++) {
					try {
						inputs[i].close();
					} 
					catch (IOException e) {
						LuiLogger.error(e);
					}
				}
			}
		}
		return null;
	}

	@Override
	public String noCompressJs(String[] jsFiles) {
		InputStream[] inputs = null;
		try{
			inputs = new FileInputStream[jsFiles.length];
			for (int i = 0; i < jsFiles.length; i++) {
				String filePath = jsFiles[i];
				File file = ContextResourceUtil.getLuiFile(filePath);
				inputs[i] = new FileInputStream(file);
			}
			return CompressorUtil.noCompressJs(inputs);
		}
		catch(Exception e){
			LuiLogger.error(e);
		}
		finally{
			if(inputs != null){
				for (int i = 0; i < inputs.length; i++) {
					try {
						inputs[i].close();
					} 
					catch (IOException e) {
						LuiLogger.error(e);
					}
				}
			}
		}
		return null;
	}
	
}
