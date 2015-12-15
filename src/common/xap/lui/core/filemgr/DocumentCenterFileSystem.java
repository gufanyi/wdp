package xap.lui.core.filemgr;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.vos.LuiFileVO;

/**
 * 使用文档中心的文件系统
 * 
 * @author licza
 *
 */
public class DocumentCenterFileSystem extends LocalFileSystem {
	public DocumentCenterFileSystem(String filePath) {
		super(filePath);
	}

	@Override
	public void deleteFile(LuiFileVO fileVO) throws Exception {
	}

	@Override
	public void deleteFile(LuiFileVO[] fileVOs) throws Exception {
		if (fileVOs == null || fileVOs.length <= 0)
			for (LuiFileVO fileVO : fileVOs) {
				deleteFile(fileVO);
			}
	}

	@Override
	public Object download(LuiFileVO fileVO, OutputStream output, long begin) throws Exception {
		return null;
	}

	@Override
	public Object download(LuiFileVO fileVO, OutputStream out) throws Exception {
		return super.download(fileVO, out);
	}

	@Override
	public boolean existInFs(String fileNo) throws Exception {
		return true;
	}

	@Override
	public void upload(LuiFileVO fileVO, InputStream input) throws Exception {
	}

	protected String createFilePath(LuiFileVO fileVersionVO) {
		if (fileVersionVO.getFilename() == null)
			return super.createFilePath(fileVersionVO);

		String id = fileVersionVO.getPk_luifile();
		if (id == null || id.length() == 0) {
			return null;
		}
		// 补齐
		int idLength = id.length() + 1;
		// K为每段的长度
		final int k = 3;
		int rootPathLength = idLength % k;
		StringBuilder sb = new StringBuilder();
		// sb.append(filePath);
		// 返回文件分隔符
		String seprator = System.getProperty("file.separator");
		if (rootPathLength != 0) {
			sb.append(seprator);
			sb.append(id.substring(0, rootPathLength));
		}
		for (int i = 0; i < idLength / k; i++) {
			sb.append(seprator);
			sb.append(StringUtils.substring(id, i * k, i * k + k));
		}
		sb.append(seprator);
		sb.append(fileVersionVO.getFilename());
		return sb.toString();
	}
}
