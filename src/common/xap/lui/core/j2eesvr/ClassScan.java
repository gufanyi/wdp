package xap.lui.core.j2eesvr;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.logger.LuiLogger;
/**
 * 扫描包下的java类
 * 
 * @author licza
 * 
 */
public class ClassScan {
	/** 文件系统 */
	public static final String URL_PROTOCOL_FILE = "file";
	/** jar */
	public static final String URL_PROTOCOL_JAR = "jar";
	/** WAS Jar协议 */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";
	/** WLS Jar协议 */
	public static final String URL_PROTOCOL_ZIP = "zip";
	/**
	 * 根据报名获取包中所有的类
	 * 
	 * @param packageName
	 * @return
	 */
	public static Set<Class<?>> getClasses(String packageName) {
		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		boolean recursive = true;
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			LuiLogger.debug(LuiRuntimeContext.getCorePath() + "=== " + packageDirName + "hasElements" + dirs.hasMoreElements());
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				/**
				 * Class文件
				 */
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else {
					/**
					 * Jar中的Class
					 */
					Enumeration<JarEntry> entries = null;
					JarFile jar = null;
					boolean isNewJar = false;
					try {
						LuiLogger.debug("====ClassScan==== protocol:" + protocol + " Jar URL : " + url.getFile());
						/**
						 * 修正在weblogic及was下无法获取jar包的bug
						 */
						if (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol) || URL_PROTOCOL_WSJAR.equals(protocol)) {
							String filepath = url.getFile();
							int idx = filepath.indexOf("!");
							/**
							 * 去掉包信息 否则Weblogic会报FileNotFoundException
							 */
							if (idx > 0)
								filepath = filepath.substring(0, filepath.indexOf("!"));
							LuiLogger.debug("====ClassScan==== new Jar URL : " + filepath);
							jar = getJarFile(filepath);
							isNewJar = true;
						}
						if (jar != null) {
							entries = jar.entries();
							// ---Modify by licza end---
							if (entries != null) {
								while (entries.hasMoreElements()) {
									// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件
									// 如META-INF等文件
									JarEntry entry = entries.nextElement();
									String name = entry.getName();
									// 如果是以/开头的
									if (name.charAt(0) == '/') {
										// 获取后面的字符串
										name = name.substring(1);
									}
									// 如果前半部分和定义的包名相同
									if (name.startsWith(packageDirName)) {
										int idx = name.lastIndexOf('/');
										// 如果以"/"结尾 是一个包
										if (idx != -1) {
											// 获取包名 把"/"替换成"."
											packageName = name.substring(0, idx).replace('/', '.');
										}
										// 如果可以迭代下去 并且是一个包
										if ((idx != -1) || recursive) {
											// 如果是一个.class文件 而且不是目录
											if (name.endsWith(".class") && !entry.isDirectory()) {
												// 去掉后面的".class" 获取真正的类名
												String className = name.substring(packageName.length() + 1, name.length() - 6);
												try {
													// 添加到classes
													LuiLogger.info("====ClassScan====" + packageName + '.' + className);
													classes.add(Class.forName(packageName + '.' + className, true, Thread.currentThread().getContextClassLoader()));
													LuiLogger.info("====ClassScan====" + packageName + '.' + className);
												} catch (Throwable e) {
													LuiLogger.error("====ClassScan====" + e.getMessage(), e);
												}
											}
										}
									}
								}
							}
						}
					} catch (IOException e) {
						LuiLogger.error(e.getMessage(), e);
					} finally {
						if (isNewJar) {
							jar.close();
						}
					}
				}
			}
		} catch (IOException e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return classes;
	}
	/**
	 * 
	 * 
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					classes.add(Class.forName(packageName + '.' + className, true, Thread.currentThread().getContextClassLoader()));
				} catch (Exception e) {
					LuiLogger.error(e.getMessage(), e);
				}
			}
		}
	}
	/**
	 * 动态获得类
	 * 
	 * @param cls
	 * @return
	 */
	public static Class<?> dynamicClass(Class<?> cls) {
		// if(RuntimeEnv.isRunningInWebSphere() ||
		// RuntimeEnv.isRunningInWeblogic()){
		// return cls;
		// }
		String clsName = cls.getCanonicalName();
		if (clsName == null) {
			return cls;
		}
		ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
		URL ctxURL = ctxLoader.getResource(clsName.replace(".", "/") + ".class");
		if (ctxURL == null)
			return cls;
		String clazzFile = ctxURL.getFile();
		// Jar包中 不再动态载入 直接返回。
		if (clazzFile != null && clazzFile.indexOf("jar!") != -1) {
			return cls;
		}
		/**
		 * 判断是否在当前的classloader中
		 */
		if (new File(clazzFile).exists()) {
			try {
				ClassLoader cl = new DynamicClassLoader(ctxLoader, clsName, ctxURL);
				return Class.forName(clsName, false, cl);
			} catch (Exception e) {
				LuiLogger.error("loader class error!");
			}
		}
		/**
		 * 遍历classloader
		 */
		String clsUrl = null;
		URL url;
		try {
			Enumeration<URL> dirs = ctxLoader.getResources(cls.getPackage().getName().replaceAll("\\.", "/"));
			while (dirs.hasMoreElements()) {
				URL urla = dirs.nextElement();
				String classFile = urla.getPath() + "/" + cls.getSimpleName() + ".class";
				clsUrl = "file://" + classFile;
				if (new File(classFile).exists()) {
					try {
						url = new URL(clsUrl);
						ClassLoader cl = new DynamicClassLoader(ctxLoader, clsName, url);
						return Class.forName(clsName, false, cl);
					} catch (Exception e2) {
						LuiLogger.debug(e2.getMessage());
					}
				}
			}
		} catch (Exception e2) {
			LuiLogger.debug(e2.getMessage());
		}
		return cls;
	}
	public static final String FILE_URL_PREFIX = "file:";
	public static JarFile getJarFile(String jarFileUrl) throws IOException {
		JarFile ufj = null;
		try {
			if (jarFileUrl.startsWith(FILE_URL_PREFIX)) {
				jarFileUrl = jarFileUrl.substring(FILE_URL_PREFIX.length());
			}
			ufj = new JarFile(new File(jarFileUrl));
			return ufj;
		} catch (IOException e) {
			LuiLogger.error(e.getMessage(), e);
			return null;
		}
	}
	/**
	 * 
	 * 
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	private static void findAndAddResourcesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<String> classes) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.isFile());
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				findAndAddResourcesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
			} else {
				String className = file.getName();
				try {
					classes.add(packageName.replace(".", "/") + '/' + className);
				} catch (Exception e) {
					LuiLogger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	public static Set<String> resourceScaner(String packageName, ClassLoader cl) {
		// 资源集合
		Set<String> resouces = new LinkedHashSet<String>();
		boolean recursive = true;
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try {
			dirs = cl.getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// java.util.logging.Logger.getLogger(PortalDeployer.class.getName()).info(url.toString());
				// 如果是以文件的形式保存在服务器上
				if (ClassScan.URL_PROTOCOL_FILE.equals(protocol)) {
					// 获取物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddResourcesInPackageByFile(packageName, filePath, recursive, resouces);
				} else {
					Enumeration<JarEntry> entries = null;
					JarFile jar = null;
					boolean isNewJar = false;
					// 如果是jar包文件
					try {
						if (ClassScan.URL_PROTOCOL_JAR.equals(protocol) || ClassScan.URL_PROTOCOL_ZIP.equals(protocol) || ClassScan.URL_PROTOCOL_WSJAR.equals(protocol)) {
							String filepath = url.getFile();
							int idx = filepath.indexOf("!");
							// 去掉包信息 否则Weblogic会报FileNotFoundException
							if (idx > 0)
								filepath = filepath.substring(0, filepath.indexOf("!"));
							jar = ClassScan.getJarFile(filepath);
						}
						if (jar != null) {
							entries = jar.entries();
							isNewJar = true;
							if (entries != null) {
								while (entries.hasMoreElements()) {
									JarEntry entry = entries.nextElement();
									String name = entry.getName();
									// 如果是以/开头的
									if (name.charAt(0) == '/') {
										// 获取后面的字符串
										name = name.substring(1);
									}
									if (name.startsWith(packageDirName)) {
										int idx = name.lastIndexOf('/');
										if (idx != -1) {
											packageName = name.substring(0, idx).replace('/', '.');
										}
										if ((idx != -1) || recursive) {
											if (!entry.isDirectory()) {
												try {
													resouces.add(name);
												} catch (Exception e) {
													LuiLogger.error(e.getMessage(), e);
												}
											}
										}
									}
								}
							}
						}
					} catch (IOException e) {
						LuiLogger.error(e.getMessage(), e);
					} finally {
						if (isNewJar) {
							if (jar != null) {
								jar.close();
								jar = null;
							}
							isNewJar = false;
						}
					}
				}
			}
		} catch (IOException e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return resouces;
	}
	
	public static void copyResourceToDir(String folder, List<String> resources, ClassLoader loader) {
		File f = new File(folder + "/");
		if (!f.exists())
			f.mkdirs();
		for (int i = 0; i < resources.size(); i++) {
			String path = resources.get(i);
			if (path != null) {
				InputStream input = null;
				OutputStream fout = null;
				try {
					if(loader.getResource(path)==null){
						continue;
					}
					java.util.logging.Logger.getAnonymousLogger().info(loader.getResource(path).toString());
					input = loader.getResourceAsStream(path);
					if (input != null) {
						String filePath = folder + "/" + path;
						String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
						File dir = new File(dirPath);
						if (!dir.exists())
							dir.mkdirs();
						fout = new FileOutputStream(filePath);
						byte[] bytes = new byte[1024 * 4];
						int count = input.read(bytes);
						while (count != -1) {
							fout.write(bytes, 0, count);
							count = input.read(bytes);
						}
					}
				} catch (Exception e) {
					LuiLogger.error(e.getMessage(), e);
				} finally {
					if (input != null)
						try {
							input.close();
						} catch (IOException e1) {
							LuiLogger.error(e1.getMessage(), e1);
						}
					if (fout != null)
						try {
							fout.close();
						} catch (IOException e) {
							LuiLogger.error(e.getMessage(), e);
						}
				}
			} else
				break;
		}
	}
}
