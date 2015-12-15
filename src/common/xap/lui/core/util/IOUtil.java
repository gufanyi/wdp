package xap.lui.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;


public  class IOUtil {

	public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static void write(byte[] input, Writer output) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(input);
		write(in, output);
	}

	public static void write(byte[] input, Writer output, String encoding)
			throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(input);
		write(in, output, encoding);
	}

	public static void write(byte[] input, OutputStream output)
			throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(input);
		write(in, output);
	}

	public static int write(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static int write(Reader input, Writer output) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static void write(InputStream input, Writer output)
			throws IOException {
		InputStreamReader in = new InputStreamReader(input);
		write(in, output);
	}

	public static void write(InputStream input, Writer output, String encoding)
			throws IOException {
		InputStreamReader in = new InputStreamReader(input, encoding);
		write(in, output);
	}

	public static void write(Reader input, OutputStream output)
			throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(output);
		write(input, out);
		// XXX Unless anyone is planning on rewriting OutputStreamWriter, we
		// have to flush here.
		out.flush();
	}

	public static void write(String input, OutputStream output)
			throws IOException {
		StringReader in = new StringReader(input);
		OutputStreamWriter out = new OutputStreamWriter(output);
		write(in, out);
		// XXX Unless anyone is planning on rewriting OutputStreamWriter, we
		// have to flush here.
		out.flush();
	}

	public static void write(String input, Writer output) throws IOException {
		output.write(input);
	}

	public static void close(Reader input) {
		if (input == null) {
			return;
		}
		try {
			input.close();
		} catch (IOException ioe) {
		}
	}

	public static void close(Writer output) {
		if (output == null) {
			return;
		}
		try {
			output.close();
		} catch (IOException ioe) {
		}
	}

	public static void close(OutputStream output) {
		if (output == null) {
			return;
		}
		try {
			output.close();
		} catch (IOException ioe) {
		}
	}

	public static void close(InputStream input) {
		if (input == null) {
			return;
		}
		try {
			input.close();
		} catch (IOException ioe) {
		}
	}

	public static String toString(InputStream input) throws IOException {
		StringWriter sw = new StringWriter();
		write(input, sw);
		return sw.toString();
	}

	public static String toString(InputStream input, String encoding)
			throws IOException {
		StringWriter sw = new StringWriter();
		write(input, sw, encoding);
		return sw.toString();
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		write(input, output);
		return output.toByteArray();
	}

	public static String toString(Reader input) throws IOException {
		StringWriter sw = new StringWriter();
		write(input, sw);
		return sw.toString();
	}

	public static byte[] toByteArray(Reader input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		write(input, output);
		return output.toByteArray();
	}

	public static byte[] toByteArray(String input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		write(input, output);
		return output.toByteArray();
	}

	public static File writeBytesToFile(byte[] filedata, String filename)
			throws IOException {
		File file = new File(filename);
		File pFile = file.getParentFile();
		if (!pFile.exists())
			pFile.mkdirs();
		FileOutputStream outStream = new FileOutputStream(file);
		try {
			outStream.write(filedata);
		} finally {
			outStream.close();
		}
		return file;
	}

	public static ByteArrayOutputStream getByteStreamFromFile(File newfile)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream stream = null;
		stream = new FileInputStream(newfile);
		int index = 0;
		try {
			while ((index = stream.read(buffer, 0, DEFAULT_BUFFER_SIZE)) > 0) {
				out.write(buffer, 0, index);
			}
		} finally {
			stream.close();
		}
		return out;
	}

	public static final Charset UTF8_CHARSET = Charset.forName("utf-8");

	public static int copy(final InputStream input, final OutputStream output)
			throws IOException {
		return copy(input, output, DEFAULT_BUFFER_SIZE);
	}

	public static int copyAndCloseInput(final InputStream input,
			final OutputStream output) throws IOException {
		try {
			return copy(input, output, DEFAULT_BUFFER_SIZE);
		} finally {
			input.close();
		}
	}

	public static int copyAndCloseInput(final InputStream input,
			final OutputStream output, int bufferSize) throws IOException {
		try {
			return copy(input, output, bufferSize);
		} finally {
			input.close();
		}
	}

	public static int copy(final InputStream input, final OutputStream output,
			int bufferSize) throws IOException {
		int avail = input.available();
		if (avail > 262144) {
			avail = 262144;
		}
		if (avail > bufferSize) {
			bufferSize = avail;
		}
		final byte[] buffer = new byte[bufferSize];
		int n = 0;
		n = input.read(buffer);
		int total = 0;
		while (-1 != n) {
			output.write(buffer, 0, n);
			total += n;
			n = input.read(buffer);
		}
		return total;
	}

	public static void copy(final Reader input, final Writer output,
			final int bufferSize) throws IOException {
		final char[] buffer = new char[bufferSize];
		int n = 0;
		n = input.read(buffer);
		while (-1 != n) {
			output.write(buffer, 0, n);
			n = input.read(buffer);
		}
	}

	public static int copyWithLimit(final InputStream input,
			final OutputStream output, long limit) throws IOException {
		int total = 0;
		while (limit > 0) {
			int b = input.read();
			if (-1 != b) {
				output.write(b);
				total++;
				limit--;
			} else {
				break;
			}
		}
		return total;
	}

	public static String readStringFromStream(InputStream in)
			throws IOException {
		return readStringFromStream(in, null);
	}

	public static String readStringFromStream(InputStream in, String charsetName)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = in.read()) != -1) {
			baos.write(i);
		}
		if (StringUtils.isNotBlank(charsetName))
			return baos.toString(charsetName);
		else
			return baos.toString();
	}

	public static byte[] readBytesFromStream(InputStream in) throws IOException {
		int i = in.available();
		if (i < DEFAULT_BUFFER_SIZE) {
			i = DEFAULT_BUFFER_SIZE;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream(i);
		copy(in, bos);
		in.close();
		return bos.toByteArray();
	}
}
