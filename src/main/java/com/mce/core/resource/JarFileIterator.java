package com.mce.core.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public final class JarFileIterator implements ResourceIterator {
	private JarInputStream jarInputStream;
	private JarEntry next;
	private Filter filter;
	private boolean start = true;

	private boolean closed = false;

	public JarFileIterator(File file, Filter filter) throws IOException {
		this(new FileInputStream(file), filter);
	}

	public JarFileIterator(InputStream is, Filter filter) throws IOException {
		this.filter = filter;
		this.jarInputStream = new JarInputStream(is);
	}

	private void setNext() {
		this.start = true;
		try {
			if (this.next != null) {
				this.jarInputStream.closeEntry();
			}
			this.next = null;
			do {
				this.next = this.jarInputStream.getNextJarEntry();
			} while ((this.next != null)
					&& ((this.next.isDirectory()) || (this.filter == null) || (!this.filter
							.accepts(this.next.getName()))));

			if (this.next == null)
				close();
		} catch (IOException e) {
			throw new RuntimeException("failed to browse jar", e);
		}
	}

	public InputStream next() {
		if ((this.closed) || ((this.next == null) && (!this.start))) {
			return null;
		}
		setNext();
		if (this.next == null) {
			return null;
		}
		return new JarInputStreamWrapper(this.jarInputStream);
	}

	public void close() {
		try {
			this.closed = true;
			this.jarInputStream.close();
		} catch (IOException ioe) {
		}
	}

	static class JarInputStreamWrapper extends InputStream {
		private InputStream is;

		public JarInputStreamWrapper(InputStream is) {
			this.is = is;
		}

		public int read() throws IOException {
			return this.is.read();
		}

		public int read(byte[] bytes) throws IOException {
			return this.is.read(bytes);
		}

		public int read(byte[] bytes, int i, int i1) throws IOException {
			return this.is.read(bytes, i, i1);
		}

		public long skip(long l) throws IOException {
			return this.is.skip(l);
		}

		public int available() throws IOException {
			return this.is.available();
		}

		public void close() throws IOException {
		}

		public void mark(int i) {
			this.is.mark(i);
		}

		public void reset() throws IOException {
			this.is.reset();
		}

		public boolean markSupported() {
			return this.is.markSupported();
		}
	}
}
