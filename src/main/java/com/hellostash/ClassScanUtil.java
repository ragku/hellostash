package com.hellostash;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassScanUtil {
	
	public static Set<Class<?>> listClasses(String packageName) throws IOException {
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
		Set<String> names = new LinkedHashSet<String> ();
		while(dirs.hasMoreElements()) {
			URL url = dirs.nextElement();
			if(url.getProtocol().equals("file")) {
				String currentPackageName = "";
				if(packageName.indexOf('.') != -1) {
					currentPackageName = packageName.substring(0, packageName.lastIndexOf('.'));
				}
				names.addAll(getClassFullName(currentPackageName, new File(url.getFile())));
			} else if(url.getProtocol().equals("jar")) {
				JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					names.addAll(getClassFullName(packageName, entry));
				}
			}
			
		}
		for(String className : names) {
			try {
				classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
			} catch (ClassNotFoundException e) {
			}
		}
		return classes;
	}
	
	public static Set<String> getClassFullName(final String packageName, File f) {
		Set<String> names = new LinkedHashSet<String> ();
		String currentPackageName = "".equals(packageName) ? "" :  packageName + ".";
		if(f.isDirectory()) {
			File[] fs = f.listFiles();
			if(null != fs && fs.length > 0) {
				for(File ft : fs) {
					names.addAll(getClassFullName(currentPackageName + f.getName(), ft));
				}
			}
		} else if(f.getName().endsWith(".class")) {
			names.add(currentPackageName + f.getName().substring(0, f.getName().length() - 6));
		}
		return names;
	}
	
	public static Set<String> getClassFullName(final String packageName, JarEntry je) {
		Set<String> names = new LinkedHashSet<String> ();
		String name = je.getName();
		if (name.charAt(0) == '/') {
			name = name.substring(1);
		}
		if(name.startsWith(packageName) && name.endsWith(".class")) {
			names.add(je.getName().substring(0, je.getName().length() - 6).replace('/', '.'));
		}
		
		return names;
	}
	
}
