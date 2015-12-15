package xap.lui.core.filemgr;

import xap.lui.core.constant.Keys;
import xap.lui.core.j2eesvr.Action;
import xap.lui.core.j2eesvr.BaseAction;
import xap.lui.core.j2eesvr.Servlet;

/**
 * 临时文件上传Action，将文件暂存到目录上，并通知前台进行业务处理
 * 
 */
@Servlet(path = "/tmpfile")
public class TempFileAction extends BaseAction {
	// private static MultipartResolver multipartResolver = new
	// CommonsMultipartResolver();;
	/**
	 * 获得MultipartHttpServletRequest
	 * 
	 * @return
	 * @throws MultipartException
	 */
	// private static MultipartHttpServletRequest
	// getMultipartResolver(HttpServletRequest request) {
	//
	// ((CommonsMultipartResolver)
	// multipartResolver).setDefaultEncoding("UTF-8");
	//
	// return multipartResolver.resolveMultipart(request);
	// }
	/**
	 * 上传文件
	 */
	@Action(method = Keys.POST)
	public void upload() {
		// InputStream input = null;
		// FileOutputStream fout = null;
		// try {
		// MultipartHttpServletRequest req = getMultipartResolver(request);
		// MultipartFile file = req.getFile("Filedata");
		// input = file.getInputStream();
		// String importDir = "importfiles";
		// String dirStr = ContextResourceUtil.getCurrentAppPath() + importDir;
		// File dir = new File(dirStr);
		// if(!dir.exists())
		// dir.mkdirs();
		// String fileName = UUID.randomUUID().toString() + ".xlsx";
		// String path = dirStr + "/" + fileName;
		// if(input != null){
		// fout = new FileOutputStream(path);
		// byte[] bytes = new byte[4096];
		// int count = input.read(bytes);
		// while(count > 0){
		// fout.write(bytes, 0, count);
		// count = input.read(bytes);
		// }
		// }
		//
		// String ctrlClazz = req.getParameter("exectrl");
		// String widgetId = req.getParameter("widgetId");
		// String method = req.getParameter("execmethod");
		// if(method == null || method.equals(""))
		// print(ctrlClazz + "," + importDir + "/" + fileName + "," + widgetId);
		// else
		// print(ctrlClazz + "," + importDir + "/" + fileName + "," + widgetId +
		// "," + method);
		//
		// } catch (Exception e) {
		// // LuiLogger.error(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
		// "TempFileAction-000005")/*文件上传失败*/, e);
		// print("文件上传失败:");
		// print(e.getMessage());
		// } finally {
		// IOUtils.closeQuietly(input);
		// IOUtils.closeQuietly(fout);
		// }
	}

}
