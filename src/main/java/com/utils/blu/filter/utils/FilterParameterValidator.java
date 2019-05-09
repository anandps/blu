package com.utils.blu.filter.utils;

import com.utils.blu.exception.InsufficientArgumentException;
import com.utils.blu.po.SingleColumnValueFilterPO;

public class FilterParameterValidator {

	public static void validateSCVFilterParameters(SingleColumnValueFilterPO scvfPO)
			throws InsufficientArgumentException {

		if (scvfPO.getColumnFamily() == null || scvfPO.getColumnFamily().isEmpty()) {
			throw new InsufficientArgumentException("Column family is empty");
		}

		if (scvfPO.getColumnQualifier() == null || scvfPO.getColumnQualifier().isEmpty()) {
			throw new InsufficientArgumentException("Column qualifier is empty");
		}

		if (scvfPO.getOperator() == null) {
			throw new InsufficientArgumentException("operator is empty");
		}

		if (scvfPO.getValue() == null) {
			throw new InsufficientArgumentException("value is empty");
		}
	}

}
