package com.mce.core.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ClassFileIterator implements ResourceIterator {
	private List<File> files;
	private int index = 0;

	public ClassFileIterator(File file, Filter filter) {
		
		this.files = new ArrayList<File>();
		try {
			init(this.files, file, filter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void init(List<File> list, File dir, Filter filter)
			throws Exception {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
//			System.out.println(files[i].getPath());
			if (files[i].isDirectory()) {
				init(list, files[i], filter);
			} else if ((filter == null)
					|| (filter.accepts(files[i].getAbsolutePath()))) {
				list.add(files[i]);
			}
		}

	}

	public final InputStream next() {
		if (this.index >= this.files.size()) {
			return null;
		}
		File fp = (File) this.files.get(this.index++);
		try {
			return new FileInputStream(fp);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
	}
}
