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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Implementation of <b>HttpServletResponseWrapper</b> that works with
 * the CompressionServletResponseStream implementation..
 *
 * @author Amy Roh
 * @author Dmitri Valdin
 * @version $Revision: 496190 $, $Date: 2007-01-14 16:21:45 -0700 (Sun, 14 Jan 2007) $
 */

public class CompressionServletResponseWrapper extends HttpServletResponseWrapper {

    // ----------------------------------------------------- Constructor

    /**
     * Calls the parent constructor which creates a ServletResponse adaptor
     * wrapping the given response object.
     */

    public CompressionServletResponseWrapper(HttpServletResponse response) {
        super(response);
        origResponse = response;
    }


    // ----------------------------------------------------- Instance Variables

    /**
     * Original response
     */

    protected HttpServletResponse origResponse = null;

    /**
     * The ServletOutputStream that has been returned by
     * <code>getOutputStream()</code>, if any.
     */

    protected ServletOutputStream stream = null;


    /**
     * The PrintWriter that has been returned by
     * <code>getWriter()</code>, if any.
     */

    protected PrintWriter writer = null;

    /**
     * Content type
     */
    protected String contentType = null;

    // --------------------------------------------------------- Public Methods


    /**
     * Set content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
        origResponse.setContentType(contentType);
    }



    /**
     * Create and return a ServletOutputStream to write the content
     * associated with this Response.
     *
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException {
        CompressionResponseStream stream = new CompressionResponseStream(origResponse);
        return stream;
    }


    /**
     * Finish a response.
     */
    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null)
                    stream.close();
            }
        } catch (IOException e) {
        }
    }


    // ------------------------------------------------ ServletResponse Methods


    /**
     * Flush the buffer and commit this response.
     *
     * @exception IOException if an input/output error occurs
     */
    public void flushBuffer() throws IOException {
    	if(stream != null)
    		((CompressionResponseStream)stream).flush();
    }

    /**
     * Return the servlet output stream associated with this Response.
     *
     * @exception IllegalStateException if <code>getWriter</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream getOutputStream() throws IOException {

        if (writer != null)
            throw new IllegalStateException("getWriter() has already been called for this response");

        if (stream == null)
            stream = createOutputStream();
        return (stream);

    }

    /**
     * Return the writer associated with this Response.
     *
     * @exception IllegalStateException if <code>getOutputStream</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public PrintWriter getWriter() throws IOException {

        if (writer != null)
            return (writer);

        if (stream != null)
            throw new IllegalStateException("getOutputStream() has already been called for this response");

        stream = createOutputStream();
        //String charset = getCharsetFromContentType(contentType);
        String charEnc = origResponse.getCharacterEncoding();
        // HttpServletResponse.getCharacterEncoding() shouldn't return null
        // according the spec, so feel free to remove that "if"
        if (charEnc != null) {
            writer = new PrintWriter(new OutputStreamWriter(stream, charEnc));
        } 
        else {
            writer = new PrintWriter(stream);
        }
        return (writer);
    }


    public void setContentLength(int length) {
    }

}
