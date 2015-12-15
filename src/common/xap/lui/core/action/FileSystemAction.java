package xap.lui.core.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.constant.Keys;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.filemgr.ZipEntry;
import xap.lui.core.filemgr.ZipOutputStream;
import xap.lui.core.j2eesvr.Action;
import xap.lui.core.j2eesvr.BaseAction;
import xap.lui.core.j2eesvr.Servlet;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.util.JsURLDecoder;

/**
 * 文件上传Action
 * 
 * @author licza
 * 
 */
@Servlet(path = "/file")
public class FileSystemAction extends BaseAction {

	/** 缓冲区大小 **/
	public static final String BILLITEM = "billitem";

	public static final String BASE_DOWN_FORDER = "d:/";

	/**
	 * 上传文件
	 */
	@Action(method = Keys.POST)
	public void upload() {
		try {
			BufferedInputStream fileIn = new BufferedInputStream(request.getInputStream());
			String fn = JsURLDecoder.decode(request.getParameter("filename"), "UTF-8");
			String forderCode = request.getParameter("fordercode");
			byte[] buf = new byte[1024];
			File forder = new File(BASE_DOWN_FORDER + forderCode);
			// 如果文件夹不存在则创建
			if (!forder.exists() && !forder.isDirectory()) {
				forder.mkdir();
			}
			File file = new File(BASE_DOWN_FORDER + forderCode + "/" + fn);
			BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(file));

			while (true) {
				// 读取数据
				int bytesIn = fileIn.read(buf, 0, 1024);
				if (bytesIn == -1) {
					break;
				} else {
					fileOut.write(buf, 0, bytesIn);
				}
			}

			fileOut.flush();
			fileOut.close();
		} catch (IOException e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}

	@Action(method = Keys.POST)
	public void delete() {
		String fn = JsURLDecoder.decode(request.getParameter("filename"), "UTF-8");
		String forderCode = request.getParameter("fordercode");
		File file = new File(BASE_DOWN_FORDER + forderCode + "/" + fn);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 通过正则表达式判断pk是否为空
	 */
	static Pattern pkPattern = Pattern.compile("^\\w{20}$");

	/**
	 * 下载文件
	 */
	@Action
	public void down() throws IOException {
		String filename = JsURLDecoder.decode(request.getParameter("filename"), "UTF-8");
		String fordercode = request.getParameter("fordercode");

		// 设置文件MIME类型
		response.setContentType(request.getServletContext().getMimeType(filename));
		// 设置Content-Disposition
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		// 读取目标文件，通过response将目标文件写到客户端
		// 获取目标文件的绝对路径
		String fullFileName = BASE_DOWN_FORDER + fordercode + "/" + filename;

		// 读取文件
		InputStream in = new FileInputStream(fullFileName);
		OutputStream out = response.getOutputStream();

		// 写文件
		int b;
		while ((b = in.read()) != -1) {
			out.write(b);
		}

		in.close();
		out.close();
	}

	/**
	 * 压缩模式下载一组文件
	 * 
	 * @param pks
	 * @throws IOException
	 */
	@Action(url = "/pack")
	public void pack() throws IOException {
		String prjRoot = JsURLDecoder.decode(request.getParameter("prjRoot"), "UTF-8");
		if (prjRoot == null)
			return;
		ZipOutputStream zo = new ZipOutputStream(response.getOutputStream());
		zo.setEncoding("gbk");
		try {
			String filenamedisplay = PaCache.getInstance().get("_projName") + ".zip";// URLEncoder.encode(filedisplay,"UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + filenamedisplay);

			File f = new File(prjRoot);
			zip(zo, f, "");
		} catch (Exception e) {
			LuiLogger.error(e.getMessage(), e);
		} finally {
			/**
			 * 安全关闭输出流
			 */
			IOUtils.closeQuietly(zo);
		}
	}

	private void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) { // 判断是否为目录
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else { // 压缩目录中的所有文件
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			System.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

}
