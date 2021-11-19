package com.jcloud.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class TypeUtil {


	public static Long toLon(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Long) {
			return (Long) obj;
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).longValue();
		} else if (obj instanceof String) {
			return Long.valueOf(obj.toString());
		} else if (obj instanceof BigDecimal) {
			return Long.valueOf(obj.toString());
		} else if (obj instanceof Integer) {
			return Long.valueOf(obj.toString());
		} else if (obj instanceof Double) {
			Double d = (Double) obj;
			return d.longValue();
		}
		return null;
	}

	public static Float toFlo(Object obj) {
		if (obj instanceof Float) {
			return (Float) obj;
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).floatValue();
		}
		return null;
	}

	public static Double toDou(Object obj) {
		if (obj instanceof Double) {
			return (Double) obj;
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).doubleValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).doubleValue();
		}
		return null;
	}

	public static String toStr(Object obj) {
		if (obj == null)
			return "";
		if (obj instanceof String) {
			return (String) obj;
		}
		return obj.toString();
	}

	public static Integer toInt(Object obj) {
		if (obj instanceof Integer) {
			return (Integer) obj;
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).intValue();
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).intValue();
		}
		return null;
	}

	public static Date toDate(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date) {
			return (Date) obj;
		}
		return null;
	}


}
