package com.dd4.common.jpa.criteria;

import java.util.Map;

public interface QueryValueTranslator {

	String translateValue(Object value);

	Map<String, Object> getParametricValues();

}
