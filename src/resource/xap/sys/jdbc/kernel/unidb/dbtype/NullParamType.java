package xap.sys.jdbc.kernel.unidb.dbtype;

/**
 * 
 * 空类�?
 * 
 *
 *
 */
public class NullParamType implements SQLParamType {
    private static final long serialVersionUID = 1L;
    int type;

    public NullParamType(int type) {
	this.type = type;
    }

    public int getType() {
	return type;
    }

    public void setType(int type) {
	this.type = type;
    }
}
