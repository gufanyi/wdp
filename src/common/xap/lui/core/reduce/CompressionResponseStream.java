/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package xap.lui.core.reduce;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * Implementation of <b>ServletOutputStream</b> that works with
 * the CompressionServletResponseWrapper implementation.
 *
 * @author Amy Roh
 * @author Dmitri Valdin
 * @version $Revision: 496190 $, $Date: 2007-01-14 16:21:45 -0700 (Sun, 14 Jan 2007) $
 */

public class CompressionResponseStream
    extends ServletOutputStream {
    // ----------------------------------------------------- Instance Variables
    
    
    /**
     * The buffer through which all of our output bytes are passed.
     */
    protected byte[] buffer = null;
    
    /**
     * The number of data bytes currently in the buffer.
     */
    protected int bufferCount = 0;
    
    protected int totalCount = 0;
    
    /**
     * The underlying gzip output stream to which we should write data.
     */
    protected OutputStream gzipstream = null;
    
    /**
     * Has this stream been closed?
     */
    protected boolean closed = false;
    
    /**
     * The content length past which we will not write, or -1 if there is
     * no defined content length.
     */
    protected int length = -1;
    
    /**
     * The response with which this servlet output stream is associated.
     */
    protected HttpServletResponse response = null;
    
    /**
     * The underlying servket output stream to which we should write data.
     */
    protected ServletOutputStream output = null;


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a servlet output stream associated with the specified Response.
     *
     * @param response The associated response
     */
    public CompressionResponseStream(HttpServletResponse response) throws IOException{
        super();
        closed = false;
        this.response = response;
        this.output = response.getOutputStream();
        buffer = new byte[4096];
    }

    public int getTotalCount() {
    	return totalCount;
    }

    /**
     * Close this output stream, causing any buffered data to be flushed and
     * any further output data to throw an IOException.
     */
    public void close() throws IOException {
        if (closed)
            throw new IOException("This output stream has already been closed");

        if (gzipstream != null) {
            flushToGZip();
            gzipstream.close();
            gzipstream = null;
        } else {
            if (bufferCount > 0) {
                output.write(buffer, 0, bufferCount);
                bufferCount = 0;
            }
        }

        output.close();
        closed = true;

    }


    /**
     * Flush any buffered data for this output stream, which also causes the
     * response to be committed.
     */
    public void flush() throws IOException {
        if (closed) {
            throw new IOException("Cannot flush a closed output stream");
        }

        if (gzipstream != null) {
            gzipstream.flush();
        }

    }

    public void flushToGZip() throws IOException {
        if (bufferCount > 0) {
            writeToGZip(buffer, 0, bufferCount);
            bufferCount = 0;
        }

    }

    /**
     * Write the specified byte to our output stream.
     *
     * @param b The byte to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(int b) throws IOException {
        if (closed)
            throw new IOException("Cannot write to a closed output stream");

        if (bufferCount >= buffer.length) {
            flushToGZip();
        }

        buffer[bufferCount++] = (byte) b;
        totalCount ++;
    }


    /**
     * Write <code>b.length</code> bytes from the specified byte array
     * to our output stream.
     *
     * @param b The byte array to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[]) throws IOException {

        write(b, 0, b.length);

    }


    /**
     * Write <code>len</code> bytes from the specified byte array, starting
     * at the specified offset, to our output stream.
     *
     * @param b The byte array containing the bytes to be written
     * @param off Zero-relative starting offset of the bytes to be written
     * @param len The number of bytes to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[], int off, int len) throws IOException {

        if (closed)
            throw new IOException("Cannot write to a closed output stream");

        if (len == 0)
            return;

        totalCount += len;
        // Can we write into buffer ?
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            return;
        }

        // There is not enough space in buffer. Flush it ...
        flushToGZip();

        // ... and try again. Note, that bufferCount = 0 here !
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            return;
        }

        // write direct to gzip
        writeToGZip(b, off, len);
    }

    public void writeToGZip(byte b[], int off, int len) throws IOException {
        if (gzipstream == null) {
            if (response.isCommitted()) {
                gzipstream = output;
            } 
            else {
                response.addHeader("Content-Encoding", "gzip");
                gzipstream = new GZIPOutputStream(output);
            }
        }
        gzipstream.write(b, off, len);
    }

    /**
     * Has this response stream been closed?
     */
    public boolean closed() {
        return (this.closed);
    }

}
