package freemarker.ext.jsp;

import freemarker.log.Logger;
import freemarker.template.TemplateModelException;

import javax.el.ELContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Implementation of PageContext that contains JSP 2.0 specific methods.
 *
 * @author Attila Szegedi
 * @version $Id: FreeMarkerPageContext2.java,v 1.1.2.1 2006/07/08 14:45:34 ddekany Exp $
 */
class FreeMarkerPageContext2 extends FreeMarkerPageContext {
    private static final Logger logger = Logger.getLogger("freemarker.jsp");

    static {
        if(JspFactory.getDefaultFactory() == null) {
            JspFactory.setDefaultFactory(new FreeMarkerJspFactory2());
        }
        logger.debug("Using JspFactory implementation class " + 
                JspFactory.getDefaultFactory().getClass().getName());
    }

    private FreeMarkerPageContext2() throws TemplateModelException {
        super();
    }

    static FreeMarkerPageContext create() throws TemplateModelException {
        return new FreeMarkerPageContext2();
    }

    /**
     * Attempts to locate and manufacture an expression evaulator instance. For this
     * to work you <b>must</b> have the Apache Commons-EL package in the classpath. If
     * Commons-EL is not available, this method will throw an UnsupportedOperationException. 
     */
    public ExpressionEvaluator getExpressionEvaluator() {
        try {
            Class type = Thread.currentThread().getContextClassLoader().loadClass
                    ("org.apache.commons.el.ExpressionEvaluatorImpl");
            return (ExpressionEvaluator) type.newInstance();
        }
        catch (Exception e) {
            throw new UnsupportedOperationException("In order for the getExpressionEvaluator() " +
                "method to work, you must have downloaded the apache commons-el jar and " +
                "made it available in the classpath.");
        }
    }

    /**
     * Returns a variable resolver that will resolve variables by searching through
     * the page scope, request scope, session scope and application scope for an
     * attribute with a matching name.
     */
    public VariableResolver getVariableResolver() {
        final PageContext ctx = this;

        return new VariableResolver() {
            public Object resolveVariable(String name) throws ELException {
                return ctx.findAttribute(name);
            }
        };
    }

    /**
     * Includes the specified path. The flush argument is ignored!
     */
    public void include(String path, boolean flush) throws IOException, ServletException {
        super.include(path);
    }

	@Override
	public ELContext getELContext() {
		// TODO Auto-generated method stub
		return null;
	}
}
