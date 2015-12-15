package xap.mw.coreitf.d;

import java.math.BigDecimal;

public class FTypeManager {
    public static Object convert2FType(FType type, Object val) {
	if (val == null) {
	    return null;
	}

	Object r = null;
	if (type == FType.FDouble) {
	    if (val instanceof FDouble) {
		r = val;
	    } else {
		r = new FDouble(val.toString());
	    }
	} else if (type == FType.FFlag || type == FType.Integer) {
	    if (val instanceof Integer) {
		r = val;
	    } else {
		r = Integer.valueOf(val.toString());
	    }
	} else if (type == FType.FStringEnum || type == FType.String) {
	    r = val.toString();
	} else if (type == FType.FBoolean) {
	    FBoolean flag = null;
	    if (val instanceof FBoolean) {
		flag = (FBoolean) val;
	    } else {
		flag = new FBoolean(val.toString());
	    }
	    if (flag.booleanValue()) {
		r = FBoolean.TRUE;
	    } else {
		r = FBoolean.FALSE;
	    }
	} else if (type == FType.FDate) {
	    if (val instanceof FDate) {
		r = val;
	    } else {

		r = FDate.fromPersisted(val.toString());
	    }
	} else if (type == FType.FDateTime) {
	    if (val instanceof FDateTime) {
		r = val;
	    } else {

		r = new FDateTime(val.toString());
	    }
	} else if (type == FType.FTime) {
	    if (val instanceof FTime) {
		r = val;
	    } else {

		r = new FTime(val.toString());
	    }
	} else if (type == FType.FLiteralDate) {
	    if (val instanceof FLiteralDate) {
		r = val;
	    } else {

		r = FLiteralDate.fromPersisted(val.toString());
	    }
	} else if (type == FType.BigDecimal) {
	    // 用FDouble支持小数位超过8位的小数,因此用FDouble来处理大小数位的字段
	    if (val instanceof FDouble) {
		r = val;
	    } else {
		r = new FDouble(val.toString());
	    }
	} else if (type == FType.Object) {
	    r = val;
	} else {
	    String message = "error!";
	    throw new IllegalArgumentException(message);
	}
	return r;
    }

    /**
     * 用于反序列化的类型转换，将c#端的普通类型转换成java端的普通类型
     * 
     * @param type
     * @param val
     * @return
     */
    public static Object convert2FType2(Class<?> type, Object val) {
	if (val == null) {
	    return null;
	}

	Object r = null;
	if (type == Integer.class) {
	    if (val instanceof Integer) {
		r = val;
	    } else {
		r = Integer.valueOf(val.toString());
	    }
	} else if (type == String.class) {
	    r = val.toString();
	} else if (type == Boolean.class) {
	    if (val instanceof Boolean) {
		r = val;
	    } else {
		r = Boolean.valueOf(val.toString());
	    }
	} else if (type == Object.class) {
	    r = val;
	} else if (type == FDouble.class) {
	    if (val instanceof FDouble) {
		r = val;
	    } else {
		r = new FDouble(val.toString());
	    }
	} else if (type == FBoolean.class) {
	    FBoolean flag = null;
	    if (val instanceof FBoolean) {
		flag = (FBoolean) val;
	    } else {
		flag = new FBoolean(val.toString());
	    }
	    if (flag.booleanValue()) {
		r = FBoolean.TRUE;
	    } else {
		r = FBoolean.FALSE;
	    }
	} else if (type == FDate.class) {
	    if (val instanceof FDate) {
		r = val;
	    } else {

		r = FDate.fromPersisted(val.toString());
	    }
	} else if (type == FDateTime.class) {
	    if (val instanceof FDateTime) {
		r = val;
	    } else {

		r = new FDateTime(val.toString());
	    }
	} else if (type == FTime.class) {
	    if (val instanceof FTime) {
		r = val;
	    } else {

		r = new FTime(val.toString());
	    }
	} else if (type == FLiteralDate.class) {
	    if (val instanceof FLiteralDate) {
		r = val;
	    } else {

		r = FLiteralDate.fromPersisted(val.toString());
	    }
	} else if (type == BigDecimal.class) {
	    // 用FDouble支持小数位超过8位的小数,因此用FDouble来处理大小数位的字段
	    if (val instanceof FDouble) {
		r = val;
	    } else {
		r = new FDouble(val.toString());
	    }
	} else {
	    String message = "error!";
	    throw new IllegalArgumentException(message);
	}
	return r;
    }
}
